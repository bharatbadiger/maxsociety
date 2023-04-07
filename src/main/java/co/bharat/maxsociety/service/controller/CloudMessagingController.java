package co.bharat.maxsociety.service.controller;

import java.io.FileInputStream;
import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import co.bharat.maxsociety.entity.GateKeepRequest;
import co.bharat.maxsociety.entity.Users;
import co.bharat.maxsociety.enums.Relationships;
import co.bharat.maxsociety.repository.UserRepository;
import co.bharat.maxsociety.response.ResponseData;

@RestController
@RequestMapping("/maxsociety/notifications")
public class CloudMessagingController {
	
	@Autowired
    private FirebaseMessaging firebaseMessaging;
	
	@Autowired
	private UserRepository userRepository;

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseData<String>> handleException(Exception ex) {
		ResponseData<String> responseData = ResponseData.<String>builder()
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(ex.getMessage()).data(null).build();
		return new ResponseEntity<>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/*
	 * @GetMapping public ResponseEntity<ResponseData<List<Circulars>>>
	 * getAllCirculars() { List<Circulars> circulars =
	 * circularsRepository.findAll(); if (circulars.isEmpty()) { return new
	 * ResponseEntity<>(new ResponseData<List<Circulars>>("No Circulars Found",
	 * HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND); } return new
	 * ResponseEntity<>(new
	 * ResponseData<List<Circulars>>("Circulars Fetched Successfully",
	 * HttpStatus.OK.value(), circulars),HttpStatus.OK); }
	 */

	@GetMapping("/{id}")
	public ResponseEntity<ResponseData<?>> getCircularByCircularId(@PathVariable Long id)
			throws IOException, FirebaseMessagingException {

		// Create new instance of Firebase App by passing appropriate credentials
		// Initialize the Firebase app
		FileInputStream serviceAccount = new FileInputStream("path/to/serviceAccountKey.json");

		FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();

		// Initialize App
		FirebaseApp.initializeApp(options);

		// new instance of the FirebaseMessaging class to send FCM messages
		//FirebaseMessaging messaging = FirebaseMessaging.getInstance();

		// Send FCM message by setting appropriate properties
		Notification notification = Notification.builder().setTitle("Title").setBody("Message body").build();

		Message message = Message.builder().setNotification(notification).setToken("DEVICE_TOKEN").build();

		firebaseMessaging.send(message);

		return new ResponseEntity<>(new ResponseData<>("Failed", HttpStatus.NOT_FOUND.value(), null),
				HttpStatus.NOT_FOUND);
	}

	
    @PostMapping("/sendNotification")
    public ResponseEntity<String> sendNotification(@Valid @RequestBody GateKeepRequest fcmRequest) {
    	Users user = userRepository.findByFlatsFlatNoAndRelationship(fcmRequest.getFlatNo(),Relationships.SELF);
        Message message = Message.builder()
                .setNotification(Notification.builder().setTitle(fcmRequest.getTitle()).setBody(fcmRequest.getBody()).build())
                .setToken(user.getFcmToken())
                .build();
        try {
            String response = firebaseMessaging.send(message);
            return ResponseEntity.ok(response);
        } catch (FirebaseMessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
