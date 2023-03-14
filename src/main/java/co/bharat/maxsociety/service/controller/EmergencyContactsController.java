package co.bharat.maxsociety.service.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.bharat.maxsociety.entity.EmergencyContacts;
import co.bharat.maxsociety.repository.EmergencyContactsRepository;
import co.bharat.maxsociety.response.ResponseData;

@RestController
@RequestMapping("/maxsociety/emergencyContacts")
public class EmergencyContactsController {

	@Autowired
	private EmergencyContactsRepository emergencyContactsRepository;

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseData<String>> handleException(Exception ex) {
		ResponseData<String> responseData = ResponseData.<String>builder()
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(ex.getMessage()).data(null).build();
		return new ResponseEntity<>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping
	public ResponseEntity<ResponseData<List<EmergencyContacts>>> getAllEmergencyContacts() {
		List<EmergencyContacts> emergencyContacts = emergencyContactsRepository.findAll();
		if (emergencyContacts.isEmpty()) {
			return new ResponseEntity<>(new ResponseData<List<EmergencyContacts>>("No EmergencyContacts Found",
					HttpStatus.NOT_FOUND.value(), null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ResponseData<List<EmergencyContacts>>("EmergencyContacts Fetched Successfully",
				HttpStatus.OK.value(), emergencyContacts), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<ResponseData<EmergencyContacts>> getEmergencyContactsById(@PathVariable Long id) {
		Optional<EmergencyContacts> existingEmergencyContacts = emergencyContactsRepository.findById(id);
		if (!existingEmergencyContacts.isPresent()) {
			return new ResponseEntity<>(new ResponseData<EmergencyContacts>("EmergencyContacts Not Found",
					HttpStatus.NOT_FOUND.value(), null), HttpStatus.NOT_FOUND);

		}
		return new ResponseEntity<>(new ResponseData<EmergencyContacts>("EmergencyContacts Fetched Successfully",
				HttpStatus.OK.value(), existingEmergencyContacts.get()), HttpStatus.OK);
	}

	@RequestMapping(value = "/society/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<ResponseData<List<EmergencyContacts>>> getEmergencyContactsBySocietyCode(
			@PathVariable Long id) {
		List<EmergencyContacts> existingEmergencyContacts = emergencyContactsRepository.findBySociety_SocietyCode(id);
		if (existingEmergencyContacts.isEmpty()) {
			return new ResponseEntity<>(new ResponseData<List<EmergencyContacts>>("EmergencyContact Not Found",
					HttpStatus.NOT_FOUND.value(), null), HttpStatus.NOT_FOUND);

		}
		return new ResponseEntity<>(new ResponseData<List<EmergencyContacts>>("EmergencyContact Fetched Successfully",
				HttpStatus.OK.value(), existingEmergencyContacts), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseData<EmergencyContacts>> createEmergencyContact(
			@RequestBody EmergencyContacts emergencyContact) {
		List<EmergencyContacts> emergencyContacts = emergencyContactsRepository.findByCategory(emergencyContact.getCategory());
		if (!emergencyContacts.isEmpty()) {
			EmergencyContacts existingEmergencyContact = emergencyContacts.get(0);
			Set<String> phoneNumbers = existingEmergencyContact.getPhoneNumbers();
			phoneNumbers.addAll(emergencyContact.getPhoneNumbers());
			emergencyContactsRepository.save(emergencyContact);
			return new ResponseEntity<>(new ResponseData<EmergencyContacts>("EmergencyContact Updated Successfully",
					HttpStatus.OK.value(), emergencyContact), HttpStatus.OK);

		}
		EmergencyContacts circular = emergencyContactsRepository.save(emergencyContact);
		return new ResponseEntity<>(new ResponseData<EmergencyContacts>("EmergencyContact created Successfully",
				HttpStatus.OK.value(), circular), HttpStatus.OK);
	}

	@PutMapping(value = { "/", "/{emergencyContactId}" })
	public ResponseEntity<ResponseData<EmergencyContacts>> updateEmergencyContact(
			@PathVariable(required = false) Long emergencyContactId, @RequestBody EmergencyContacts emergencyContact) {
		if (emergencyContactId == null) {
			emergencyContactId = emergencyContact.getId();
		}
		Optional<EmergencyContacts> existingCircular = emergencyContactsRepository.findById(emergencyContactId);
		if (!existingCircular.isPresent()) {
			return new ResponseEntity<>(new ResponseData<EmergencyContacts>("EmergencyContact Not Found",
					HttpStatus.NOT_FOUND.value(), null), HttpStatus.NOT_FOUND);

		}
		EmergencyContacts updatedEmergencyContacts = emergencyContactsRepository.save(emergencyContact);
		return new ResponseEntity<>(new ResponseData<EmergencyContacts>("EmergencyContact Updated Successfully",
				HttpStatus.OK.value(), updatedEmergencyContacts), HttpStatus.OK);
	}

	@DeleteMapping("/{emergencyContactId}")
	public ResponseEntity<Void> deleteEmergencyContact(@PathVariable Long emergencyContactId,
			@RequestParam(name = "phoneNumber", required = false) String phoneNumber) {
		Optional<EmergencyContacts> emergencyContact = emergencyContactsRepository.findById(emergencyContactId);
		if (!emergencyContact.isPresent()) {
			return ResponseEntity.notFound().build();

		}
		Set<String> phoneNumbers = emergencyContact.get().getPhoneNumbers();

		if (phoneNumbers.contains(phoneNumber)) {
			if(phoneNumbers.size()==1) {
				emergencyContactsRepository.deleteById(emergencyContact.get().getId());
			} else {
				phoneNumbers.remove(phoneNumber);
				emergencyContact.get().setPhoneNumbers(phoneNumbers);
				emergencyContactsRepository.save(emergencyContact.get());
			}
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}