package be.kennyverheyden.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import be.kennyverheyden.services.UserService;

@ControllerAdvice
public class GlobalControllerAdvice {

	@Autowired
	private UserService userService;

	public GlobalControllerAdvice() {}

	
	// Check logged in status
	@ModelAttribute("isLoggedIn")
	public boolean isLoggedIn()
	{

		if(userService.getUserEmail()!=null && userService.getSecret()!=null)
		{
			return true;
		}
		else
		{
			// User is not logged in
			return false;
		}
	}

}

