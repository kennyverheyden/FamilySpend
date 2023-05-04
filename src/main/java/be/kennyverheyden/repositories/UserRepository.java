package be.kennyverheyden.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import be.kennyverheyden.models.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	User findUserByeMail (String eMail);
	
	@Modifying
	@Query(value="DELETE FROM tblBook WHERE tblBook.userFK = ?",nativeQuery = true)
	void deleteBookingsFromUser(Long userFK);
	
	@Modifying
	@Query(value="DELETE FROM tblCategory WHERE tblCategory.userFK = ?",nativeQuery = true)
	void deleteCategoriesFromUser(Long userFK);
	
	@Modifying
	@Query(value="DELETE FROM tblGroup WHERE tblGroup.userFK = ?",nativeQuery = true)
	void deleteGroupsFromUser(Long userFK);

}


