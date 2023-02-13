package co.bharat.maxsociety.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.bharat.maxsociety.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {

	Users save(Optional<Users> updateUser);
	List<Users> findByFlatsFlatNo(String flatNo);

}
