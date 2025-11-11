package com.esameIts.CharmApp.services;

import com.esameIts.CharmApp.ENUM.BookingStatus;
import com.esameIts.CharmApp.dto.BookingDto;
import com.esameIts.CharmApp.dto.CreateBookingRequest;
import com.esameIts.CharmApp.entities.Booking;
import com.esameIts.CharmApp.entities.ServiceItem;
import com.esameIts.CharmApp.entities.User;
import com.esameIts.CharmApp.mappers.BookingMapper;
import com.esameIts.CharmApp.repositories.BookingRepository;
import com.esameIts.CharmApp.repositories.ClosureRepository;
import com.esameIts.CharmApp.repositories.ServiceItemRepository;
import com.esameIts.CharmApp.repositories.UserRepository;
import com.esameIts.CharmApp.repositories.WorkingHourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

	private final UserRepository userRepo;
	private final ServiceItemRepository serviceRepo;
	private final BookingRepository bookingRepo;
	private final WorkingHourRepository workingHourRepo;
	private final ClosureRepository closureRepo;

	/**
	 * Restituisce le prenotazioni dell'utente loggato.
	 */
	@Transactional(readOnly = true)
	public List<BookingDto> getMyBookings(String userEmail) {
		User me = userRepo.findByEmail(userEmail)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utente non trovato"));
		return BookingMapper.toDtoList(bookingRepo.findByUserIdOrderByDateDescStartTimeDesc(me.getId()));
	}

	/**
	 * Crea una nuova prenotazione per l'utente loggato.
	 */
	@Transactional
	public BookingDto create(String userEmail, CreateBookingRequest in) {
		// utente
		User me = userRepo.findByEmail(userEmail)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utente non trovato"));

		// servizio
		ServiceItem svc = serviceRepo.findById(in.getServiceId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servizio non trovato"));

		// parsing data/ora
		LocalDate date;
		LocalTime start;
		try {
			date = LocalDate.parse(in.getDate()); // formato: YYYY-MM-DD
			start = LocalTime.parse(in.getStartTime()); // formato: HH:mm
		} catch (DateTimeParseException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato data/ora non valido");
		}

		// calcolo orario di fine
		LocalTime end = start.plusMinutes(svc.getDurationMin() + svc.getBufferMin());

		// controlli apertura/chiusura
		ensureWithinOpening(date, start, end);

		// controlla sovrapposizione (capacità = 1)
		if (bookingRepo.existsOverlap(date, start, end)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot non disponibile");
		}

		// crea e salva la prenotazione
		Booking b = Booking.builder().user(me).service(svc).date(date).startTime(start).endTime(end)
				.status(BookingStatus.CONFIRMED).notes(in.getNotes()).build();

		return BookingMapper.toDto(bookingRepo.save(b));
	}

	/**
	 * Controlla se data e orari rientrano nelle aperture del salone, considerando
	 * anche eventuali chiusure straordinarie.
	 */
	private void ensureWithinOpening(LocalDate date, LocalTime start, LocalTime end) {
		DayOfWeek dow = date.getDayOfWeek();

		// orari normali
		var whOpt = workingHourRepo.findByDay(dow);
		if (whOpt.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Giorno chiuso");
		}
		LocalTime open = whOpt.get().getOpen();
		LocalTime close = whOpt.get().getClose();

		// eventuali chiusure straordinarie
		var closureOpt = closureRepo.findByDate(date);
		if (closureOpt.isPresent()) {
			var c = closureOpt.get();
			if (c.getOpenOverride() == null && c.getCloseOverride() == null) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Giorno chiuso");
			}
			if (c.getOpenOverride() != null)
				open = c.getOpenOverride();
			if (c.getCloseOverride() != null)
				close = c.getCloseOverride();
		}

		// controllo finale
		if (start.isBefore(open) || end.isAfter(close)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Orario fuori dalle aperture");
		}
	}

	/**
	 * Restituisce tutte le prenotazioni (solo per ADMIN).
	 */
	@Transactional(readOnly = true)
	public List<BookingDto> findAll() {
		return BookingMapper.toDtoList(bookingRepo.findAll());
	}

	/**
	 * Cancellazione prenotazione (per CLIENTE).
	 */
	@Transactional
	public void deleteMyBooking(Long bookingId, String userEmail) {
		User me = userRepo.findByEmail(userEmail)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utente non trovato"));

		Booking b = bookingRepo.findById(bookingId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prenotazione non trovata"));

		if (!b.getUser().getId().equals(me.getId())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Non puoi cancellare questa prenotazione");
		}

		bookingRepo.delete(b);
	}

	/**
	 * Cancellazione prenotazione (per ADMIN).
	 */
	@Transactional
	public void deleteByAdmin(Long bookingId) {
		Booking b = bookingRepo.findById(bookingId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prenotazione non trovata"));
		bookingRepo.delete(b);
	}

}
