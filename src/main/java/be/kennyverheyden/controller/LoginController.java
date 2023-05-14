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
public class LoginController{

	@Autowired
	private LoginProcessor loginProcessor;
	@Autowired
	private UserService userService;

	public LoginController() {}

	@GetMapping("/login") // get request
	public String loginGet(Model model) {
		model.addAttribute("content", "login");
		return "index";
	}

	@PostMapping("/login/submit") 
	public String loginPost(@RequestParam String userEmail, @RequestParam String secret, Model model, RedirectAttributes rm) {
		boolean loggedIn = false;

		try {
			loggedIn = loginProcessor.login(userEmail, secret);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(loggedIn == true)
		{
			userService.clearToken(userService.findUserByeMail(userEmail)); // Clear password reset (in mail) token in case never used
			model.addAttribute("content", "main");
			return "redirect:/main";
		}
		else
		{
			model.addAttribute("content", "login");
			rm.addFlashAttribute("message","Your credentials are incorrect");
			return "redirect:/login";
		}
	}

}