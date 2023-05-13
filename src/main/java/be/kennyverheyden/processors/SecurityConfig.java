package be.kennyverheyden.processors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); 
	}

		@Bean
		public SpringSecurityDialect springSecurityDialect(){
			return new SpringSecurityDialect();
		}

		
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.authorizeHttpRequests()
		.requestMatchers("/").permitAll()
		.requestMatchers("/w3.css").permitAll()
		.requestMatchers("/login").permitAll()
		.requestMatchers("/login/submit").permitAll()
		.requestMatchers("/signup").permitAll()
		.requestMatchers("/signup/register").permitAll()
		.requestMatchers("/home").permitAll()
		.requestMatchers("/forgot_password").permitAll()
		.requestMatchers("/reset_password_form").permitAll()
		.requestMatchers("/forgot_password_form").permitAll()
		.requestMatchers("/forgot_password_reset_output").permitAll()
		.requestMatchers("/process_register").permitAll()
		.requestMatchers("/account").permitAll()
		.requestMatchers("/verify_fail").permitAll()
		.requestMatchers("/verify_success").permitAll()
		.requestMatchers("/privacy").permitAll()
		.requestMatchers("/book").authenticated()
		.requestMatchers("/category").authenticated()
		.requestMatchers("/categorytotals").authenticated()
		.requestMatchers("/group").authenticated()
		.requestMatchers("/grouptotals").authenticated()
		.requestMatchers("/main").authenticated()
		.requestMatchers("/passreset").authenticated()
		.requestMatchers("/admin").hasAnyAuthority("ADMIN")
		.and()
		.formLogin()
		.loginPage("/login")
		.loginProcessingUrl("/login")
		.defaultSuccessUrl("/login")
		.permitAll();
		//.and()
		//.logout()
		//.logoutRequestMatcher(new AntPathRequestMatcher("/main?logout"))
		//.logoutSuccessUrl("/")
		//.invalidateHttpSession(true)
	//	.permitAll();
		return http.build();
	}



}
