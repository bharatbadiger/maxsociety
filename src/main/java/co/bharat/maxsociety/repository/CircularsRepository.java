package co.bharat.maxsociety.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.bharat.maxsociety.entity.Circulars;
import co.bharat.maxsociety.enums.CircularType;

@Repository
public interface CircularsRepository extends JpaRepository<Circulars, String> {
	Long countByCircularType(CircularType circularType);
	List<Circulars> findByCircularType(CircularType circulartype);
	List<Circulars> findByCreatedBy(String createdby);
	List<Circulars> findBySociety_SocietyCode(Long societyCode);
}