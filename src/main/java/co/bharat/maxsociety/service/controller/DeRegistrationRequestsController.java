package co.bharat.maxsociety.service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.bharat.maxsociety.entity.DeRegistrationRequest;
import co.bharat.maxsociety.entity.Users;
import co.bharat.maxsociety.repository.DeRegistrationRequestRepository;
import co.bharat.maxsociety.repository.UserRepository;
import co.bharat.maxsociety.request.DTO.DeRegistrationRequestDTO;
import co.bharat.maxsociety.response.ResponseData;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/maxsociety/dereg")
public class DeRegistrationRequestsController {
	private final DeRegistrationRequestRepository deRegistrationRequestRepository;
	@Autowired
	private UserRepository userRepository;

	public DeRegistrationRequestsController(DeRegistrationRequestRepository deRegistrationRequestRepository) {
		this.deRegistrationRequestRepository = deRegistrationRequestRepository;
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseData<String>> handleException(Exception ex) {
		ResponseData<String> responseData = ResponseData.<String>builder()
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(ex.getMessage()).data(null).build();
		return new ResponseEntity<>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<ResponseData<DeRegistrationRequest>> getUser(@PathVariable Long id) {
		Optional<DeRegistrationRequest> deRegRequest = deRegistrationRequestRepository.findById(id);
		if (!deRegRequest.isPresent()) {
			return new ResponseEntity<>(
					new ResponseData<DeRegistrationRequest>("No DeRegistrationRequest Found", HttpStatus.NOT_FOUND.value(), null),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ResponseData<DeRegistrationRequest>(
				"DeRegistrationRequest Fetched Successfully", HttpStatus.OK.value(), deRegRequest.get()), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<ResponseData<List<DeRegistrationRequest>>> getAllDeregistrationRequests(
			@RequestParam(name = "id", required = false) Long id,
			@RequestParam(name = "userId", required = false) String userId,
			@RequestParam(name = "status", required = false) String status,
			@RequestParam(name = "createdBy", required = false) String createdBy) {

		Specification<DeRegistrationRequest> spec = Specification.where(null);

		if (id != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("id"), id));
		}

		if (userId != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("userId"), userId));
		}
		
		if (createdBy != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("createdBy"), createdBy));
		}

		if (status != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
		}

		List<DeRegistrationRequest> deRegRequests = deRegistrationRequestRepository.findAll(spec);
		if (deRegRequests.isEmpty()) {
			return new ResponseEntity<>(
					new ResponseData<List<DeRegistrationRequest>>("No DeRegistrationRequests Found", HttpStatus.NOT_FOUND.value(), null),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ResponseData<List<DeRegistrationRequest>>(
				"DeRegistrationRequests Fetched Successfully", HttpStatus.OK.value(), deRegRequests), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseData<DeRegistrationRequest>> createUser(
			@RequestBody DeRegistrationRequestDTO deRegistrationRequestDTO) {
		try {
			Optional<Users> user = userRepository.findById(deRegistrationRequestDTO.getUserId());
			if (!user.isPresent()) {
				return new ResponseEntity<>(
						new ResponseData<DeRegistrationRequest>("No User Found", HttpStatus.NOT_FOUND.value(), null),
						HttpStatus.NOT_FOUND);
			}
			/*
			 * Users user = userRepository.getById(deRegistrationRequestDTO.getUserId()); if
			 * (user!=null && user.getUserId()!=null) { return new ResponseEntity<>(new
			 * ResponseData<DeRegistrationRequest>("User Not Found",
			 * HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND); }
			 */
			DeRegistrationRequest deregRequest = DeRegistrationRequest.builder().user(user.get())
					.createdBy(deRegistrationRequestDTO.getCreatedBy())
					.updatedBy(deRegistrationRequestDTO.getUpdatedBy()).status(deRegistrationRequestDTO.getStatus())
					.build();
			DeRegistrationRequest deregRequestDetails = deRegistrationRequestRepository.save(deregRequest);
			return new ResponseEntity<>(
					new ResponseData<DeRegistrationRequest>("DeRegistrationRequest created Successfully",
							HttpStatus.OK.value(), deregRequestDetails),
					HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseData<DeRegistrationRequest>("Request Failed",
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PutMapping(value = { "/", "/{id}" })
	public ResponseEntity<ResponseData<DeRegistrationRequest>> updateDeRegistrationRequest(
			@PathVariable(required = false) Long id, @RequestBody DeRegistrationRequestDTO deRegistrationRequestDTO) {
		try {
			Optional<DeRegistrationRequest> deRegistrationRequest = deRegistrationRequestRepository.findById(id);
			if (!deRegistrationRequest.isPresent()) {
				return new ResponseEntity<>(new ResponseData<DeRegistrationRequest>("No DeRegistrationRequest Found",
						HttpStatus.NOT_FOUND.value(), null), HttpStatus.NOT_FOUND);
			}
			DeRegistrationRequest deRegRequest = deRegistrationRequest.get();
			deRegRequest.setStatus(deRegistrationRequestDTO.getStatus());
			deRegRequest.setUpdatedBy(deRegistrationRequestDTO.getUpdatedBy());
			DeRegistrationRequest deregRequestDetails = deRegistrationRequestRepository.save(deRegRequest);
			return new ResponseEntity<>(
					new ResponseData<DeRegistrationRequest>("DeRegistrationRequest updated Successfully",
							HttpStatus.OK.value(), deregRequestDetails),
					HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseData<DeRegistrationRequest>("Request Failed",
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
		deRegistrationRequestRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
