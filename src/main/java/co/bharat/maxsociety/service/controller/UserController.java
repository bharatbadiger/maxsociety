package co.bharat.maxsociety.service.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import co.bharat.maxsociety.entity.Users;
import co.bharat.maxsociety.enums.ERole;
import co.bharat.maxsociety.enums.Relationships;
import co.bharat.maxsociety.repository.UserRepository;
import co.bharat.maxsociety.response.AdditionalResponseData;
import co.bharat.maxsociety.response.ResponseData;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/maxsociety/users")
public class UserController {
	private final UserRepository userRepository;

	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseData<String>> handleException(Exception ex) {
		ResponseData<String> responseData = ResponseData.<String>builder()
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(ex.getMessage()).data(null).build();
		return new ResponseEntity<>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/{id}/{imei}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<ResponseData<Users>> getUser(@PathVariable String id,
			@PathVariable(required = false) String imei) {
		Optional<Users> user1 = userRepository.findById(id);
		if (!user1.isPresent()) {
			return new ResponseEntity<>(new ResponseData<Users>("No User Found", HttpStatus.NOT_FOUND.value(), null),
					HttpStatus.NOT_FOUND);
		}
		if (user1.get().getImei() == null || user1.get().getImei().isEmpty()) {
			if (imei != null) {
				user1.get().setImei(imei);
				userRepository.save(user1.get());
				return new ResponseEntity<>(
						new ResponseData<Users>("User Fetched Successfully and Imei updated", HttpStatus.OK.value(), user1.get()),
						HttpStatus.OK);

			} else {
				return new ResponseEntity<>(
						new ResponseData<Users>("No Imei Provided", HttpStatus.NOT_FOUND.value(), null),
						HttpStatus.NOT_FOUND);
			}
		} else if(user1.get().getImei().equals(imei)){
			return new ResponseEntity<>(
					new ResponseData<Users>("User Fetched Successfully", HttpStatus.OK.value(), user1.get()),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ResponseData<Users>("No User Found", HttpStatus.NOT_FOUND.value(), null),
					HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping
	public ResponseEntity<ResponseData<List<Users>>> getUsersByRoleAndRelationship(
			@RequestParam(name = "role", required = false) ERole roleName,
			@RequestParam(name = "societyCode", required = false) Long societyCode,
			@RequestParam(name = "relation", required = false) Relationships relationship,
			@RequestParam(name = "imei", required = false) String imei,
			@RequestParam(name = "userId", required = false) String userId) {

		List<Users> users;
		if (roleName != null && societyCode != null && relationship != null) {
			// Fetch users by all three criteria
			users = userRepository.findByRolesNameAndFlatsSocietySocietyCodeAndRelationship(roleName, societyCode,
					relationship);
		} else if (roleName != null && societyCode != null) {
			// Fetch users by roleName and societyCode
			users = userRepository.findByRolesNameAndFlatsSocietySocietyCode(roleName, societyCode);
		} else if (roleName != null && relationship != null) {
			// Fetch users by roleName and relationship
			users = userRepository.findByRolesNameAndRelationship(roleName, relationship);
		} else if (societyCode != null && relationship != null) {
			// Fetch users by societyCode and relationship
			users = userRepository.findByFlatsSocietySocietyCodeAndRelationship(societyCode, relationship);
		} else if (userId != null && imei != null) {
			// Fetch users by societyCode and relationship
			users = userRepository.findByUserIdAndImei(userId, imei);
		} else if (roleName != null) {
			// Fetch users by roleName
			users = userRepository.findByRolesName(roleName);
		} else if (societyCode != null) {
			// Fetch users by societyCode
			users = userRepository.findByFlatsSocietySocietyCode(societyCode);
		} else if (relationship != null) {
			// Fetch users by relationship
			users = userRepository.findByRelationship(relationship);
		} else if (userId != null) { 
			// Fetch users by Id 
			Optional<Users> user1 = userRepository.findById(userId); 
			users = user1.map(Collections::singletonList).orElse(Collections.emptyList());
		} else {
			// Return all users if no params are specified
			users = userRepository.findAll();
		}

		if (users.isEmpty()) {
			return new ResponseEntity<>(
					new ResponseData<List<Users>>("No Users Found", HttpStatus.NOT_FOUND.value(), users),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(
				new ResponseData<List<Users>>("Users Fetched Successfully", HttpStatus.OK.value(), users),
				HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseData<Users>> createUser(@RequestBody Users user) {
		Users existingUser = userRepository.findByFlatsFlatNoAndRelationship(user.getFlats().getFlatNo(),
				Relationships.SELF);
		if (existingUser != null) {
			return new ResponseEntity<>(new ResponseData<Users>("Flat already has a SELF user registered",
					HttpStatus.CONFLICT.value(), null), HttpStatus.CONFLICT);

		}
		Users newUser = userRepository.save(user);
		return new ResponseEntity<>(
				new ResponseData<Users>("User Created Successfully", HttpStatus.OK.value(), newUser), HttpStatus.OK);
	}

	@PutMapping(value = { "/", "/{id}" })
	public ResponseEntity<AdditionalResponseData<?>> updateUser(@PathVariable(required = false) String id,
			@RequestBody Users user) {
		boolean isImeiUpdated = false;
		if (id == null) {
			id = user.getUserId();
		}
		Optional<Users> existingUser = userRepository.findById(id);
		if (!existingUser.isPresent()) {
			return new ResponseEntity<>(
					new AdditionalResponseData<Users>("User Not Found", HttpStatus.NOT_FOUND.value(), null, null),
					HttpStatus.NOT_FOUND);

		}
		if ((existingUser.get().getImei() == null || existingUser.get().getImei().isEmpty())
				&& user.getImei() != null) {
			isImeiUpdated = true;
		}
		Users updatedUser = userRepository.save(user);
		if (isImeiUpdated) {
			// JSONObject newResponse = new JSONObject();
			Map<String, Object> newResponse = new HashMap<String, Object>();
			newResponse.put("isImeiAdded", "true");
			return new ResponseEntity<>(new AdditionalResponseData<>("User Updated Successfully", HttpStatus.OK.value(),
					newResponse, updatedUser), HttpStatus.OK);

		}
		return new ResponseEntity<>(
				new AdditionalResponseData<>("User Updated Successfully", HttpStatus.OK.value(), null, updatedUser),
				HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
		userRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
