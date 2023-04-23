package be.kennyverheyden.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import be.kennyverheyden.models.Book;
import be.kennyverheyden.models.GroupedCategory;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findBookByUserUserID(Long userID);
	Book findBookBybookID(Long bookID);
}
