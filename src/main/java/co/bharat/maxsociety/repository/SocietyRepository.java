package co.bharat.maxsociety.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.bharat.maxsociety.entity.Society;

@Repository
public interface SocietyRepository extends JpaRepository<Society, Long> {
	
}
