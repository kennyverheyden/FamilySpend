package be.kennyverheyden.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import be.kennyverheyden.models.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Long>{
}