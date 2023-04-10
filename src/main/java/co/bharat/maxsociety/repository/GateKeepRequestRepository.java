package co.bharat.maxsociety.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.bharat.maxsociety.entity.GateKeepRequest;
import java.lang.String;
import java.util.List;

@Repository
public interface GateKeepRequestRepository extends JpaRepository<GateKeepRequest, Long> {
	
	List<GateKeepRequest> findByFlatNo(String flatno);

	Page<GateKeepRequest> findByFlatNo(String flatNo, Pageable pageable);;
}
