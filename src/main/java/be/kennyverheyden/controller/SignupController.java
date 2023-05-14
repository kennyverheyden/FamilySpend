package be.kennyverheyden.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import be.kennyverheyden.services.CurrencyService;
import be.kennyverheyden.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SignupController {

	@Autowired
	private UserService userService;

	@Autowired
	private CurrencyService currencyService; // Ask preferred currency

	

	public SignupController() {}

	@GetMapping("/signup") // get request
	public String signupGet(Model model) {

		model.addAttribute("currencies",currencyService.findAllCurrencies()); // Option for currency
		model.addAttribute("content", "signup");
		return "index";
	}

	@PostMapping("/signup/register") 
	public String signupUserPost(@RequestParam (required = false) String userEmail, @RequestParam (required = false) String secret, @RequestParam (required = false) String confirmSecret, @RequestParam (required = false) String name, @RequestParam (required = false) String firstName, @RequestParam (required = false) Long currencyFK, HttpServletRequest request, Model model, RedirectAttributes rm)throws UnsupportedEncodingException, MessagingException
	{

		Long role=2L; // 2 = user
		if(!userEmail.equals("") && !secret.equals("") && !confirmSecret.equals("") && !name.equals("") && !firstName.equals(""))
		{
			if(!userService.userExist(userEmail))
			{
				if(secret.equals(confirmSecret))
				{
					//boolean loggedIn = false;
					// Register user
					userService.register(userEmail, secret, name, firstName, role, currencyFK, getSiteURL(request));
					model.addAttribute("content", "process_register");
					rm.addFlashAttribute("welcomeName",firstName);
					return "redirect:/process_register";
				}
				else
				{
					// Check if password boxes are the same
					model.addAttribute("content", "signup");
					rm.addFlashAttribute("message","Confirmation password not the same");
					return "redirect:/signup";
				}
			}
			else
			{
				// Check if all fields are filled in
				model.addAttribute("content", "signup");
				rm.addFlashAttribute("message","Email is already registered");
				return "redirect:/signup";
			}
		}
		else
		{
			// Check if all fields are filled in
			model.addAttribute("content", "signup");
			rm.addFlashAttribute("message","Please fill in all fields");
			return "redirect:/signup";
		}
	}

	@GetMapping("/process_register")
	public String processRegisterGet(Model model) {
		model.addAttribute("content", "process_register");
		return "index";
	}

	@GetMapping("/verify")
	public String verifyUser(@Param("code") String code, Model model) {
		if (userService.verify(code)) {
			model.addAttribute("content", "verify_success");
			return "index";
		} else {
			model.addAttribute("content", "verify_fail");
			return "index";
		}
	}

	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}
}
