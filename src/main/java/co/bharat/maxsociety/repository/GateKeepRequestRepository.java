package co.bharat.maxsociety.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.bharat.maxsociety.entity.GateKeepRequest;

@Repository
public interface GateKeepRequestRepository extends JpaRepository<GateKeepRequest, Long> {

}
