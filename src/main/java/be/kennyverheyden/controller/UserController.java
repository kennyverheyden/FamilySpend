package be.kennyverheyden.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import be.kennyverheyden.services.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	public UserController() {}

}
