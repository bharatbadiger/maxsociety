package co.bharat.maxsociety.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.bharat.maxsociety.entity.Flats;

@Repository
public interface FlatsRepository extends JpaRepository<Flats, String> {
	
}
