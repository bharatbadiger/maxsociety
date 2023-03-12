package co.bharat.maxsociety.service.controller;

import java.util.List;
import java.util.Optional;

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

import co.bharat.maxsociety.entity.CircularImage;
import co.bharat.maxsociety.entity.Circulars;
import co.bharat.maxsociety.entity.Society;
import co.bharat.maxsociety.entity.Users;
import co.bharat.maxsociety.enums.CircularType;
import co.bharat.maxsociety.repository.CircularsRepository;
import co.bharat.maxsociety.repository.SocietyRepository;
import co.bharat.maxsociety.repository.UserRepository;
import co.bharat.maxsociety.response.ResponseData;
import co.bharat.maxsociety.service.CircularService;

@RestController
@RequestMapping("/maxsociety/circulars")
public class CircularsController {
	private final CircularsRepository circularsRepository;

	public CircularsController(CircularsRepository circularsRepository) {
		this.circularsRepository = circularsRepository;
	}
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SocietyRepository societyRepository;
	
	@Autowired
    private CircularService circularService;

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
	public ResponseEntity<ResponseData<List<Circulars>>> getAllCirculars() {
		List<Circulars> circulars = circularsRepository.findAll();
	    if (circulars.isEmpty()) {
	        return new ResponseEntity<>(new ResponseData<List<Circulars>>("No Circulars Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	    }		
		return new ResponseEntity<>(new ResponseData<List<Circulars>>("Circulars Fetched Successfully", HttpStatus.OK.value(), circulars),HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseData<Circulars>> getCircularByCircularId(@PathVariable Long id) {
		Optional<Circulars> circulars = circularsRepository.findById(id);
	    if (!circulars.isPresent()) {
	        return new ResponseEntity<>(new ResponseData<Circulars>("No Circulars Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	    }		
		return new ResponseEntity<>(new ResponseData<Circulars>("Circular Fetched Successfully", HttpStatus.OK.value(), circulars.get()),HttpStatus.OK);
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<ResponseData<List<Circulars>>>  getCircularByUserId(@PathVariable String id) {
		List<Circulars> existingCirculars = circularsRepository.findByCreatedBy(id);
		if (existingCirculars.isEmpty()) {
	        return new ResponseEntity<>(new ResponseData<List<Circulars>>("Circular Not Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	        
	    }
		return new ResponseEntity<>(new ResponseData<List<Circulars>>("Circulars Fetched Successfully", HttpStatus.NOT_FOUND.value(), existingCirculars), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/society/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<ResponseData<List<Circulars>>>  getCircularsBySocietyCode(@PathVariable Long id) {
		List<Circulars> existingCirculars = circularsRepository.findBySociety_SocietyCode(id);
		if (existingCirculars.isEmpty()) {
	        return new ResponseEntity<>(new ResponseData<List<Circulars>>("Circular Not Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	        
	    }
		return new ResponseEntity<>(new ResponseData<List<Circulars>>("Circulars Fetched Successfully", HttpStatus.OK.value(), existingCirculars), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/type/{circularType}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<ResponseData<List<Circulars>>>  getCircularsByCircularType(@PathVariable CircularType circularType) {
		List<Circulars> existingCirculars = circularsRepository.findByCircularType(circularType);
		if (existingCirculars.isEmpty()) {
	        return new ResponseEntity<>(new ResponseData<List<Circulars>>("No Circulars Found for the Type", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	        
	    }
		return new ResponseEntity<>(new ResponseData<List<Circulars>>("Circulars Fetched Successfully", HttpStatus.OK.value(), existingCirculars), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<ResponseData<Circulars>> createCircular( @RequestBody Circulars circulars) {
		if(circulars.getSociety()==null) {
			Optional<Society> society = societyRepository.findById(1L);
			if(society.isPresent()) {
				circulars.setSociety(society.get());
			}
		}
		for (CircularImage circularImage : circulars.getCircularImages()) {
            circularImage.setCircular(circulars);
        }
		Optional<Users> user = userRepository.findById(String.valueOf(circulars.getCreatedBy().getUserId()));
		if(!user.isPresent()) {
			return new ResponseEntity<>(new ResponseData<Circulars>("No User Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
		}
		circulars.setCreatedBy(user.get());
		circulars.setUpdatedBy(user.get());
		if (!String.valueOf(circulars.getCreatedBy()).equalsIgnoreCase(String.valueOf(circulars.getUpdatedBy()))) {
			Optional<Users> user2 = userRepository.findById(String.valueOf(circulars.getCreatedBy().getUserId()));
			if (!user.isPresent()) {
				return new ResponseEntity<>(
						new ResponseData<Circulars>("No User Found", HttpStatus.NOT_FOUND.value(), null),
						HttpStatus.NOT_FOUND);
			}
			circulars.setUpdatedBy(user2.get());
		}
		Circulars circular = circularService.createCircular(circulars);
		return new ResponseEntity<>(new ResponseData<Circulars>("Circular created Successfully", HttpStatus.OK.value(), circular),HttpStatus.OK);
	}

	@PutMapping(value = {"/", "/{circularId}"})
	public ResponseEntity<ResponseData<Circulars>> updateCircular(@PathVariable(required = false) Long circularId, @RequestBody Circulars circulars) {
		if(circularId == null) {
			circularId=circulars.getCircularId();
		}
		Optional<Circulars> existingCircular = circularsRepository.findById(circularId);
		if (!existingCircular.isPresent()) {
	        return new ResponseEntity<>(new ResponseData<Circulars>("Circular Not Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	        
	    }
		Circulars updatedCirculars = circularsRepository.save(circulars);
		return new ResponseEntity<>(new ResponseData<Circulars>("Circular Updated Successfully", HttpStatus.OK.value(), updatedCirculars),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCircular(@PathVariable("id") Long id) {
	    circularsRepository.deleteById(id);
	    return ResponseEntity.noContent().build();
	}

}