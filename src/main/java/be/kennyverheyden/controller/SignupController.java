package be.kennyverheyden.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.kennyverheyden.processors.LoginProcessor;
import be.kennyverheyden.services.UserService;

@Controller
public class SignupController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private LoginProcessor loginProcessor;

	public SignupController() {}
	
	@GetMapping("/signup") // get request
	public String signupGet(Model model) {
		String userEmail = userService.getUserEmail();


		model.addAttribute("content", "signup");
		return "index";
	}

	@PostMapping("/signup") 
	public String signupUserPost(@RequestParam (required = false) String userEmail, @RequestParam (required = false) String secret, @RequestParam (required = false) String confirmSecret, @RequestParam (required = false) String name, @RequestParam (required = false) String firstName, Model model, RedirectAttributes rm){

		Long role=2L; // 2 = user
		if(!userEmail.equals("") && !secret.equals("") && !confirmSecret.equals("") && !name.equals("") && !firstName.equals(""))
		{
			if(!userService.userExist(userEmail))
			{
				if(secret.equals(confirmSecret))
				{
					boolean loggedIn = false;
					
					// Register user
					userService.signupUser(userEmail, secret, name, firstName, role);
					loginProcessor.setUserEmail(userEmail);
					loginProcessor.setSecret(secret);
					loggedIn = loginProcessor.login();

					return "redirect:main";
				}
				else
				{
					// Check if password boxes are the same
					model.addAttribute("content", "signup");
					rm.addFlashAttribute("message","Confirmation password not the same");
					return "redirect:signup";
				}
			}
			else
			{
				// Check if all fields are filled in
				model.addAttribute("content", "signup");
				rm.addFlashAttribute("message","Email is already registered");
				return "redirect:signup";
			}
		}
		else
		{
			// Check if all fields are filled in
			model.addAttribute("content", "signup");
			rm.addFlashAttribute("message","Please fill in all fields");
			return "redirect:signup";
		}
	}
}
