package com.esameIts.CharmApp.services;


import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.esameIts.CharmApp.dto.LoginRequest;
import com.esameIts.CharmApp.dto.RegisterRequest;
import com.esameIts.CharmApp.dto.TokenResponse;
import com.esameIts.CharmApp.entities.Role;
import com.esameIts.CharmApp.entities.User;
import com.esameIts.CharmApp.repositories.RoleRepository;
import com.esameIts.CharmApp.repositories.UserRepository;
import com.esameIts.CharmApp.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
  private final UserRepository userRepo;
  private final PasswordEncoder encoder;
  private final JwtService jwt;
  private final RoleRepository roleRepo;

  public TokenResponse login(LoginRequest req) {
	  
    var u = userRepo.findByEmail(req.getEmail())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utente non trovato"));
    if (!encoder.matches(req.getPassword(), u.getPasswordHash()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenziali non valide");

    System.out.println("Plain: " + req.getPassword());
    System.out.println("Hash in DB: " + u.getPasswordHash());
    System.out.println("Match? " + encoder.matches(req.getPassword(), u.getPasswordHash()));

    String access = jwt.generateAccessToken(u);
    return new TokenResponse(access, null);
  }
  
  public void register(RegisterRequest req) {
      // email già registrata?
      if (userRepo.findByEmail(req.getEmail()).isPresent()) {
          throw new ResponseStatusException(HttpStatus.CONFLICT, "Email già registrata");
      }

      // recupera ruolo USER
      Role userRole = roleRepo.findByName("USER")
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ruolo USER non trovato"));

      // crea utente
      User user = User.builder()
              .email(req.getEmail())
              .passwordHash(encoder.encode(req.getPassword()))
              .name(req.getName())
              .enabled(true)
              .build();

      // assegna ruolo USER
      user.getRoles().add(userRole);

      userRepo.save(user);
  }

}