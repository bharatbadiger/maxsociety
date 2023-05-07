package co.bharat.maxsociety.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.bharat.maxsociety.entity.EmergencyContacts;
import java.lang.String;

@Repository
public interface EmergencyContactsRepository extends JpaRepository<EmergencyContacts, Long> {

	List<EmergencyContacts> findBySociety_SocietyCode(Long id);
	List<EmergencyContacts> findByCategory(String category);

}
