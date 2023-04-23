package be.kennyverheyden.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import be.kennyverheyden.models.GroupedCategory;

@Repository
public interface GroupedCategoryRepository extends JpaRepository<GroupedCategory, Long> {
	
	@Query(value="select bookID as id, categoryName, sum(amount) as total from tblBook join tblCategory on categoryFK = categoryID where tblBook.userFK=? group by tblCategory.categoryID",nativeQuery = true)
	List<GroupedCategory> groupedCategories(Long userID);
	
}
