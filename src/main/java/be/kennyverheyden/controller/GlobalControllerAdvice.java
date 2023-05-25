package be.kennyverheyden.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.annotation.SessionScope;

import be.kennyverheyden.models.Month;
import be.kennyverheyden.processors.UserDetailsImpl;

@SessionScope
@ControllerAdvice
public class GlobalControllerAdvice {

	public GlobalControllerAdvice() {}

	@Autowired
	UserDetailsImpl userDetails;

	// Check loggedIn status
	@ModelAttribute("isLoggedIn")
	public boolean isLoggedIn()
	{
		if(userDetails.getUser()!=null)
		{
			return true;
		}
		else
		{
			// User is not logged in
			return false;
		}
	}

	// Check isAdmin role
	@ModelAttribute("isAdmin")
	public boolean isAdmin()
	{
		if(userDetails.getUser()!=null)
		{
			if(userDetails.getUser().getUserRole().getRoleID()==1) {
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	// Used for html select option text and values
	@ModelAttribute("months")
	public List<Month> Months()
	{
		List<Month> months = new ArrayList();
		months.add(new Month("January","01"));
		months.add(new Month("February","02"));
		months.add(new Month("March","03"));
		months.add(new Month("April","04"));
		months.add(new Month("May","05"));
		months.add(new Month("June","06"));
		months.add(new Month("July","07"));
		months.add(new Month("August","08"));
		months.add(new Month("September","09"));
		months.add(new Month("October","10"));
		months.add(new Month("November","11"));
		months.add(new Month("December","12"));
		return months;
	}

}

