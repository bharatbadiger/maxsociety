package co.bharat.maxsociety.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.bharat.maxsociety.entity.Circulars;
import co.bharat.maxsociety.entity.Users;
import co.bharat.maxsociety.enums.CircularType;

@Repository
public interface CircularsRepository extends JpaRepository<Circulars, Long> {
	Long countByCircularType(CircularType circularType);
	List<Circulars> findByCircularTypeOrderByUpdatedOnDesc(CircularType circulartype);
	List<Circulars> findByCreatedByOrderByUpdatedOnDesc(Users id);
	List<Circulars> findBySociety_SocietyCodeOrderByUpdatedOnDesc(Long societyCode);
	List<Circulars> findByCircularIdAndCreatedByUserIdAndCircularTypeOrderByUpdatedOnDesc(Long circularId, String createdBy,CircularType circularType);
	List<Circulars> findByCreatedByUserIdAndCircularTypeOrderByUpdatedOnDesc(String createdBy, CircularType circularType);
	List<Circulars> findByCircularIdAndCreatedByUserIdOrderByUpdatedOnDesc(Long circularId, String createdBy);
	List<Circulars> findByCircularIdAndCircularTypeOrderByUpdatedOnDesc(Long circularId, CircularType circularType);
	List<Circulars> findByCreatedByUserIdOrderByUpdatedOnDesc(String createdBy);
	List<Circulars> findByCircularIdOrderByUpdatedOnDesc(Long circularId);
	List<Circulars> findByOrderByUpdatedOnDesc();
}