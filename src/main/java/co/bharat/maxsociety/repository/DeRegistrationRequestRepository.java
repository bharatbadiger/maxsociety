package co.bharat.maxsociety.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.bharat.maxsociety.entity.DeRegistrationRequest;

@Repository
public interface DeRegistrationRequestRepository extends JpaRepository<DeRegistrationRequest, Long> {

	List<DeRegistrationRequest> findAll(Specification<DeRegistrationRequest> spec);


}
