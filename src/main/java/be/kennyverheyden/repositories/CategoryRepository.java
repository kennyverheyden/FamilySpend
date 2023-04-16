package be.kennyverheyden.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.kennyverheyden.models.Category;
import be.kennyverheyden.models.Group;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	List<Category> findCategoryByCategoryName(String categoryName);
	List<Category> findCategoryByUserUserID(Long userID);
	Category findCategoryByCategoryID(Long categoryID);

}
