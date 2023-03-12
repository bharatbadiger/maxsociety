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
import co.bharat.maxsociety.entity.Society;
import co.bharat.maxsociety.entity.Users;
import co.bharat.maxsociety.repository.FlatsRepository;
import co.bharat.maxsociety.repository.SocietyRepository;
import co.bharat.maxsociety.repository.UserRepository;
import co.bharat.maxsociety.response.ResponseData;

@RestController
@RequestMapping("/maxsociety/flats")
public class FlatsController {
	@Autowired
	private FlatsRepository flatsRepository;
	@Autowired
	private UserRepository userRepository;
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
	public ResponseEntity<ResponseData<List<Flats>>> getAllApartments() {
		List<Flats> flats = flatsRepository.findAll();
	    if (flats.isEmpty()) {
	        return new ResponseEntity<>(new ResponseData<List<Flats>>("No Flats Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	    }		
		return new ResponseEntity<>(new ResponseData<List<Flats>>("Flats Fetched Successfully", HttpStatus.OK.value(), flats),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<ResponseData<Flats>>  getFlatByFlatNo(@PathVariable String id) {
		Optional<Flats> existingFlat = flatsRepository.findById(id);
		if (!existingFlat.isPresent()) {
	        return new ResponseEntity<>(new ResponseData<Flats>("Flat Not Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	        
	    }
		return new ResponseEntity<>(new ResponseData<Flats>("Flat Fetched Successfully", HttpStatus.OK.value(), existingFlat.get()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/members/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<ResponseData<List<Users>>>  getUsersByFlatNo(@PathVariable String id) {
		List<Users> users = userRepository.findByFlatsFlatNo(id);
		return new ResponseEntity<>(new ResponseData<List<Users>>("Users Fetched Successfully", HttpStatus.OK.value(), users), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseData<Flats>> createApartment(@Valid @RequestBody Flats apartment) {
		if(apartment.getSociety()==null) {
			Optional<Society> society = societyRepository.findById(1L);
			if(society.isPresent()) {
				apartment.setSociety(society.get());
			}
		}
		Flats flat = flatsRepository.save(apartment);
		return new ResponseEntity<>(new ResponseData<Flats>("Flat Created Successfully", HttpStatus.OK.value(), flat),HttpStatus.OK);
	}
	
	@PostMapping("/list")
	public ResponseEntity<ResponseData<List<Flats>>> createFlats(@Valid @RequestBody List<Flats> flatsList) {
		for (Flats flats : flatsList) {
			if(flats.getSociety()==null) {
				Optional<Society> society = societyRepository.findById(1L);
				if(society.isPresent()) {
					flats.setSociety(society.get());
				}
			}
		}
	    try {
	        List<Flats> flats = flatsRepository.saveAll(flatsList);
	        return new ResponseEntity<>(new ResponseData<>("Flats Created Successfully", HttpStatus.OK.value(), flats),HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(new ResponseData<>("Error Creating Flats", HttpStatus.INTERNAL_SERVER_ERROR.value(), null),HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@PutMapping(value = {"/", "/{flatCode}"})
	public ResponseEntity<ResponseData<Flats>> updateApartment(@PathVariable(required = false) String flatCode,@RequestBody Flats apartment) {
		if(flatCode == null) {
			flatCode=apartment.getFlatNo();
		}
		Optional<Flats> existingFlats = flatsRepository.findById(flatCode);
		if (!existingFlats.isPresent()) {
	        return new ResponseEntity<>(new ResponseData<Flats>("Flat Not Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	        
	    }
		Flats updatedFlat = flatsRepository.save(apartment);
		return new ResponseEntity<>(new ResponseData<Flats>("Flat Updated Successfully", HttpStatus.OK.value(), updatedFlat),HttpStatus.OK);
	
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFlats(@PathVariable("id") String id) {
		flatsRepository.deleteById(id);
	    return ResponseEntity.noContent().build();
	}
}