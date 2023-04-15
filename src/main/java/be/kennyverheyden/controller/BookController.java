package be.kennyverheyden.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import be.kennyverheyden.services.UserService;


@Controller
public class BookController {

	@Autowired
	private UserService userService;

	public BookController() {}

	@GetMapping("/book")
	public String bookGet(Model model)
	{

		String userEmail = userService.getUserEmail();
		//		// When user is not logged on, the String is null

//		if(userEmail==null)
//		{
//			model.addAttribute("content", "login");
//			return "redirect:/";
//		}

		model.addAttribute("content", "book");
		return "index";
	}

}
