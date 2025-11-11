package com.esameIts.CharmApp.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {
	@GetMapping("/me")
	  public String me(Authentication auth) { return "Ciao " + auth.getName(); }
}
