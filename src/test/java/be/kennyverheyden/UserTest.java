package be.kennyverheyden;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import be.kennyverheyden.models.User;
import be.kennyverheyden.services.UserService;

@SpringBootTest
public class UserTest {

	@Autowired
	UserService testUserService;
	
	@Test
	void findUserByeMail() {
		User users = testUserService.findUserByeMail("test@test.com");
		assertEquals("TestName", users.getName());
	}
	
}
