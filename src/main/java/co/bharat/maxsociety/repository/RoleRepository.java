package co.bharat.maxsociety.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.bharat.maxsociety.entity.Role;
import co.bharat.maxsociety.entity.Users;
import co.bharat.maxsociety.enums.ERole;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

	Role findByName(ERole role);
	//List<Users> findByNameUsers(ERole roleName);
	

}
