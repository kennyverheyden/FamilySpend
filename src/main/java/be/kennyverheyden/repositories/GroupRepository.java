package be.kennyverheyden.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import be.kennyverheyden.models.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
	List<Group> findGroupByGroupName(String groupName);
	Group findGroupByGroupID(Long groupID);
	List<Group> findGroupByUserUserID(Long userID);
	
}
