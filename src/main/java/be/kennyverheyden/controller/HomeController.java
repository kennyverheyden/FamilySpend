package be.kennyverheyden.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	public HomeController() {}

	@GetMapping("/")
	public String homeGet(Model model) {

		model.addAttribute("content", "home");
		return "index";
	}

	@GetMapping("/privacy") //
	public String privacyGet(Model model) {
		model.addAttribute("content", "privacy");
		return "index";
	}

}
