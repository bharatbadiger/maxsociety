package co.bharat.maxsociety.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.bharat.maxsociety.entity.Vehicles;

@Repository
public interface VehiclesRepository extends JpaRepository<Vehicles, String> {

	List<Vehicles> findByFlats_FlatNo(String flatNo);
	

}
