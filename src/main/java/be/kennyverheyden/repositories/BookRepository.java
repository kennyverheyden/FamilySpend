package be.kennyverheyden.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import be.kennyverheyden.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findBookByUserUserID(Long userID);
	Book findBookBybookID(Long bookID);
}
