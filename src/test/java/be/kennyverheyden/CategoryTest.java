package be.kennyverheyden;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import be.kennyverheyden.models.Category;
import be.kennyverheyden.services.CategoryService;

@SpringBootTest
public class CategoryTest {

	@Autowired
	CategoryService testCategoryService;
	
	@Test
	void findCategoryByCategoryID()
	{
		Category category = testCategoryService.findCategoryByCategoryID(52L);
		assertEquals("Wage",category.getCategoryName());
	}
	
	@Test
	void CheckCategoryWithUser()
	{
		List<Category> cats = testCategoryService.findCategoryByUserUserID(9L);
		assertEquals("Wage",cats.get(1).getCategoryName());
	}
	
	
}
