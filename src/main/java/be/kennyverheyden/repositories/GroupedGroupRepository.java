package be.kennyverheyden.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import be.kennyverheyden.models.GroupedGroup;

@Repository
public interface GroupedGroupRepository extends JpaRepository<GroupedGroup, Long> {

	@Query(value="select bookID as id, tblGroup.groupName, sum(amount) as total from tblBook join tblCategory on categoryFK = categoryID join tblGroup on tblGroup.groupID = tblCategory.groupFK where tblBook.userFK=? group by tblCategory.categoryID",nativeQuery = true)
	List<GroupedGroup> groupedGroups(Long userID);
	
}
