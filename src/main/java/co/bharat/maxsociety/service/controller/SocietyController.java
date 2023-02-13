package co.bharat.maxsociety.service.controller;

import java.util.List;
import java.util.Optional;

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

import co.bharat.maxsociety.entity.Society;
import co.bharat.maxsociety.repository.SocietyRepository;
import co.bharat.maxsociety.response.ResponseData;

@RestController
@RequestMapping("/maxsociety/society")
public class SocietyController {
	private final SocietyRepository societyRepository;

	public SocietyController(SocietyRepository societyRepository) {
		this.societyRepository = societyRepository;
	}

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
	public ResponseEntity<ResponseData<List<Society>>> getAllSociety() {
		List<Society> users = societyRepository.findAll();
	    if (users.isEmpty()) {
	        return new ResponseEntity<>(new ResponseData<List<Society>>("No Societies Found", HttpStatus.NOT_FOUND.value(), users),HttpStatus.NOT_FOUND);
	    }		
		return new ResponseEntity<>(new ResponseData<List<Society>>("Societies Fetched Successfully", HttpStatus.OK.value(), users),HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<ResponseData<Society>>  getSocietyBySocietyCode(@PathVariable Long id) {
		Optional<Society> existingSociety = societyRepository.findById(id);
		if (!existingSociety.isPresent()) {
	        return new ResponseEntity<>(new ResponseData<Society>("Society Not Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	        
	    }
		return new ResponseEntity<>(new ResponseData<Society>("Society Fetched Successfully", HttpStatus.OK.value(), existingSociety.get()),HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<ResponseData<Society>> createSociety(@RequestBody Society society) {
		return new ResponseEntity<>(new ResponseData<Society>("Society Created Successfully", HttpStatus.OK.value(), societyRepository.save(society)),HttpStatus.OK);
	}

	@PutMapping(value = {"/", "/{societyCode}"})
	public ResponseEntity<ResponseData<Society>> updateSociety(@PathVariable(required = false) Long societyCode, @RequestBody Society society) {

		if(societyCode == null) {
			societyCode=society.getSocietyCode();
		}
		Optional<Society> existingSociety = societyRepository.findById(societyCode);
		if (!existingSociety.isPresent()) {
	        return new ResponseEntity<>(new ResponseData<Society>("Society Not Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	        
	    }
		Society updatedSociety = societyRepository.save(society);
		return new ResponseEntity<>(new ResponseData<Society>("Society Updated Successfully", HttpStatus.OK.value(), updatedSociety),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSociety(@PathVariable("id") Long id) {
		societyRepository.deleteById(id);
	    return ResponseEntity.noContent().build();
	}
}