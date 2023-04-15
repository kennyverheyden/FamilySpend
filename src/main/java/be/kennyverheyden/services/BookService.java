package be.kennyverheyden.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.kennyverheyden.models.Book;
import be.kennyverheyden.models.Category;
import be.kennyverheyden.models.Group;
import be.kennyverheyden.repositories.BookRepository;
import be.kennyverheyden.repositories.CategoryRepository;
import be.kennyverheyden.repositories.GroupRepository;
import be.kennyverheyden.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private UserRepository userRepository;
	
	public BookService() {}

	
	public List<Book> findBookByUserUserID (Long userID) {
		return bookRepository.findBookByUserUserID(userID);
	}
}
