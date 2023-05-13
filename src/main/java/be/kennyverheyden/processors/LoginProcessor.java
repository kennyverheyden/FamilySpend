package be.kennyverheyden.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import be.kennyverheyden.services.UserService;

@Component
public class LoginProcessor {

	@Autowired
	private AuthenticationConfiguration authenticationConfiguration;
	@Autowired
	private UserService userService;
	
	
	public LoginProcessor () {}


	public boolean login(String userEmail, String secret) throws Exception
	{
		Authentication auth = new UsernamePasswordAuthenticationToken(userEmail,secret);
		AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
		auth= authenticationManager.authenticate(auth);

		SecurityContext sc = SecurityContextHolder.getContext();
		sc.setAuthentication(auth);
		System.out.println(sc.getAuthentication().getName());
		System.out.println(sc.getAuthentication().getAuthorities());
		secret=null;
		userService.setUserEmail(userEmail);
		System.out.println(auth.isAuthenticated());
		return auth.isAuthenticated();
	}

}
