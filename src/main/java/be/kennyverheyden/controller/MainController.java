package be.kennyverheyden.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import be.kennyverheyden.models.User;
import be.kennyverheyden.processors.UserDetailsImpl;
import be.kennyverheyden.services.UserService;

@Controller
public class MainController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserDetailsImpl userDetails;

	public MainController() {}

	@GetMapping("/main")
	public String mainGet(@RequestParam(required = false)String logout, Model model)
	{
		User user=userService.findUserByeMail(userDetails.getUsername()); // Get user information
		model.addAttribute("welcomeName",user.getFirstName());
		model.addAttribute("content", "main");
		return "index";
	}

}
