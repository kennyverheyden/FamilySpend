package be.kennyverheyden.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import be.kennyverheyden.services.UserService;

@Controller
public class HomeController {

	@Autowired
	UserService userService;

	public HomeController() {}

	// **** AUTO LOGIN test account ****
	boolean doAutoTestLogin(boolean on)
	{
		if(on && userService.getUserEmail()==null && userService.getSecret()==null){
			userService.setUserEmail("test@test.com");
			userService.setSecret("test");
			System.out.println("AUTOLOGON testaccount = activated");
			return true; }
		return false;
	}


	@GetMapping("/")
	public String homeGet(Model model) {

		model.addAttribute("content", "home");

		// ***** AUTO LOGIN test account *****
		if(doAutoTestLogin(false))
			return "redirect:/";
		// ***********************************

		return "index";
	}

	@GetMapping("/privacy") //
	public String privacyGet(Model model) {
		model.addAttribute("content", "privacy");
		return "index";
	}

}
