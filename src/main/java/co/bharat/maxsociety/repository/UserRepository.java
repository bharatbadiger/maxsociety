package co.bharat.maxsociety.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.bharat.maxsociety.entity.Users;
import co.bharat.maxsociety.enums.ERole;
import co.bharat.maxsociety.enums.Relationships;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {

	Users save(Optional<Users> updateUser);
	List<Users> findByFlatsFlatNo(String flatNo);
	List<Users> findByRolesName(String roleName);
	List<Users> findByRolesName(ERole role);
	List<Users> findByRolesNameAndFlatsSocietySocietyCodeAndRelationship(ERole roleName, Long societyCode, Relationships relationship);
	List<Users> findByRolesNameAndFlatsSocietySocietyCode(ERole roleName, Long societyCode);
	List<Users> findByRolesNameAndRelationship(ERole roleName, Relationships relationship);
	List<Users> findByFlatsSocietySocietyCodeAndRelationship(Long societyCode, Relationships relationship);
	List<Users> findByFlatsSocietySocietyCode(Long societyCode);
	List<Users> findByRelationship(Relationships relationship);
	long countByFlats_FlatNo(String flatNo);
	Users findByFlatsFlatNoAndRelationship(String flatNo, Relationships relationship);
	/*
	 * @Query("SELECT u FROM Users u LEFT JOIN FETCH u.vehicles WHERE u.userId = :userId"
	 * ) Users findUserByIdWithVehicles(@Param("userId") Long userId);
	 * 
	 * @Query("SELECT u FROM Users u LEFT JOIN FETCH u.vehicles LEFT JOIN FETCH u.familyMembers WHERE u.userId = :userId"
	 * ) Users findUserByIdWithVehiclesAndFamily(@Param("userId") Long userId);
	 */
}
