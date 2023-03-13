package co.bharat.maxsociety.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.bharat.maxsociety.entity.GalleryItems;

@Repository
public interface GalleryItemsRepository extends JpaRepository<GalleryItems, Long> {

	List<GalleryItems> findBySociety_SocietyCodeOrderByUpdatedOnDesc(Long id);

	List<GalleryItems> findByOrderByUpdatedOnDesc();

}
