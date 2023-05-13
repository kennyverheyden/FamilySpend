package be.kennyverheyden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


@EnableMethodSecurity(securedEnabled=true) // Method security
@EnableGlobalAuthentication
@SpringBootApplication()
public class FamilySpendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FamilySpendApplication.class, args);
        
        
    }
}
