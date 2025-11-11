package com.esameIts.CharmApp.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.esameIts.CharmApp.entities.Role;
import com.esameIts.CharmApp.entities.User;
import com.esameIts.CharmApp.repositories.RoleRepository;
import com.esameIts.CharmApp.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DevBootstrap implements CommandLineRunner {

  private final UserRepository userRepo;
  private final RoleRepository roleRepo;
  private final PasswordEncoder encoder;

  @Override
  public void run(String... args) {
    // Assicurati che i ruoli esistano
    Role adminRole = roleRepo.findByName("ADMIN")
        .orElseGet(() -> roleRepo.save(Role.builder().name("ADMIN").build()));
    Role userRole = roleRepo.findByName("USER")
        .orElseGet(() -> roleRepo.save(Role.builder().name("USER").build()));

    // Crea ADMIN solo se non esiste
    userRepo.findByEmail("admin@charmapp.local").ifPresentOrElse(u -> {}, () -> {
      User admin = User.builder()
          .email("admin@charmapp.local")
          .passwordHash(encoder.encode("admin"))  // password: admin
          .name("Admin")
          .enabled(true)
          .build();
      admin.getRoles().add(adminRole);
      userRepo.save(admin);
    });

    // Crea USER demo solo se non esiste
    userRepo.findByEmail("user@charmapp.local").ifPresentOrElse(u -> {}, () -> {
      User user = User.builder()
          .email("user@charmapp.local")
          .passwordHash(encoder.encode("user"))  // password: user
          .name("User")
          .enabled(true)
          .build();
      user.getRoles().add(userRole);
      userRepo.save(user);
    });
  }
}
