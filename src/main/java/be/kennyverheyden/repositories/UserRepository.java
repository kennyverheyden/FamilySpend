package be.kennyverheyden.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.kennyverheyden.models.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	User findUserByeMail (String eMail);
}


