package co.bharat.maxsociety.service.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.bharat.maxsociety.entity.Flats;
import co.bharat.maxsociety.entity.GalleryItems;
import co.bharat.maxsociety.entity.Society;
import co.bharat.maxsociety.repository.GalleryItemsRepository;
import co.bharat.maxsociety.repository.SocietyRepository;
import co.bharat.maxsociety.response.ResponseData;

@RestController
@RequestMapping("/maxsociety/galleryItems")
public class GalleryItemsController {
	
	@Autowired
	private GalleryItemsRepository galleryItemsRepository;
	
	@Autowired
	private SocietyRepository societyRepository;
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseData<String>> handleException(Exception ex) {
	    ResponseData<String> responseData = ResponseData.<String>builder()
	            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
	            .message(ex.getMessage())
	            .data(null)
	            .build();
	    return new ResponseEntity<>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping
	public ResponseEntity<ResponseData<List<GalleryItems>>> getAllGalleryItems() {
		List<GalleryItems> circulars = galleryItemsRepository.findAll();
	    if (circulars.isEmpty()) {
	        return new ResponseEntity<>(new ResponseData<List<GalleryItems>>("No GalleryItems Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	    }		
		return new ResponseEntity<>(new ResponseData<List<GalleryItems>>("GalleryItems Fetched Successfully", HttpStatus.OK.value(), circulars),HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<ResponseData<GalleryItems>>  getGalleryItemsById(@PathVariable Long id) {
		Optional<GalleryItems> existingGalleryItems = galleryItemsRepository.findById(id);
		if (!existingGalleryItems.isPresent()) {
	        return new ResponseEntity<>(new ResponseData<GalleryItems>("GalleryItems Not Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	        
	    }
		return new ResponseEntity<>(new ResponseData<GalleryItems>("GalleryItems Fetched Successfully", HttpStatus.OK.value(), existingGalleryItems.get()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/society/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<ResponseData<List<GalleryItems>>>  getGalleryItemsBySocietyCode(@PathVariable Long id) {
		List<GalleryItems> existingGalleryItems = galleryItemsRepository.findBySociety_SocietyCode(id);
		if (existingGalleryItems.isEmpty()) {
	        return new ResponseEntity<>(new ResponseData<List<GalleryItems>>("Circular Not Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	        
	    }
		return new ResponseEntity<>(new ResponseData<List<GalleryItems>>("Circular Fetched Successfully", HttpStatus.OK.value(), existingGalleryItems), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<ResponseData<GalleryItems>> createGalleryItem( @RequestBody GalleryItems galleryItem) {
		if(galleryItem.getSociety()==null) {
			Optional<Society> society = societyRepository.findById(1L);
			if(society.isPresent()) {
				galleryItem.setSociety(society.get());
			}
		}
		GalleryItems circular = galleryItemsRepository.save(galleryItem);
		return new ResponseEntity<>(new ResponseData<GalleryItems>("GalleryItem created Successfully", HttpStatus.OK.value(), circular),HttpStatus.OK);
	}
	
	@PostMapping("/list")
	public ResponseEntity<ResponseData<List<GalleryItems>>> createFlats(@Valid @RequestBody List<GalleryItems> flatsList) {
		for (GalleryItems flats : flatsList) {
			if(flats.getSociety()==null) {
				Optional<Society> society = societyRepository.findById(1L);
				if(society.isPresent()) {
					flats.setSociety(society.get());
				}
			}
		}
	    try {
	        List<GalleryItems> flats = galleryItemsRepository.saveAll(flatsList);
	        return new ResponseEntity<>(new ResponseData<>("GalleryItem Created Successfully", HttpStatus.OK.value(), flats),HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(new ResponseData<>("Error Creating GalleryItem", HttpStatus.INTERNAL_SERVER_ERROR.value(), null),HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@PutMapping(value = {"/", "/{circularId}"})
	public ResponseEntity<ResponseData<GalleryItems>> updateGalleryItem(@PathVariable(required = false) Long galleryItemId, @RequestBody GalleryItems galleryItem) {
		if(galleryItemId == null) {
			galleryItemId=galleryItem.getGalleryItemId();
		}
		Optional<GalleryItems> existingCircular = galleryItemsRepository.findById(galleryItemId);
		if (!existingCircular.isPresent()) {
	        return new ResponseEntity<>(new ResponseData<GalleryItems>("GalleryItem Not Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	        
	    }
		GalleryItems updatedGalleryItems = galleryItemsRepository.save(galleryItem);
		return new ResponseEntity<>(new ResponseData<GalleryItems>("GalleryItem Updated Successfully", HttpStatus.OK.value(), updatedGalleryItems),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteGalleryItem(@PathVariable("id") Long id) {
	    galleryItemsRepository.deleteById(id);
	    return ResponseEntity.noContent().build();
	}

}