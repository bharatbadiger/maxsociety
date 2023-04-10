package co.bharat.maxsociety.service.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import co.bharat.maxsociety.entity.GateKeepRequest;
import co.bharat.maxsociety.entity.Users;
import co.bharat.maxsociety.enums.Relationships;
import co.bharat.maxsociety.repository.GateKeepRequestRepository;
import co.bharat.maxsociety.repository.UserRepository;
import co.bharat.maxsociety.response.ResponseData;

@RestController
@RequestMapping("/maxsociety/notifications")
public class CloudMessagingController {
	
	@Autowired
    private FirebaseMessaging firebaseMessaging;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private GateKeepRequestRepository gateKeepRequestRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(CloudMessagingController.class);
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseData<String>> handleException(Exception ex) {
		ResponseData<String> responseData = ResponseData.<String>builder()
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(ex.getMessage()).data(null).build();
		LOGGER.error("Error encountered : "+ex.getMessage(),ex);
		return new ResponseEntity<>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/getNotifications")
	public ResponseEntity<ResponseData<?>> getNotifications(@RequestParam(defaultValue = "0", name = "pageNumber", required = false) int pageNumber,
	        @RequestParam(defaultValue = "100", name = "pageSize", required = false) int pageSize, @RequestParam(name = "flatNo", required = false) String flatNo)
			throws IOException, FirebaseMessagingException {
		Sort sort = Sort.by("gkReqInitTime").descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<GateKeepRequest> pageResult;
		if(flatNo !=null) {
			pageResult = gateKeepRequestRepository.findByFlatNo(flatNo,pageable);
		} else {
			pageResult = gateKeepRequestRepository.findAll(pageable);
		}
		
		Map<String, Object> response = new HashMap<>();
	    response.put("gateKeepRequests", pageResult.getContent());
	    response.put("currentPage", pageResult.getNumber());
	    response.put("totalItems", pageResult.getTotalElements());
	    response.put("totalPages", pageResult.getTotalPages());
		return new ResponseEntity<>(
				new ResponseData<>("Notifications sent successfully", HttpStatus.OK.value(), response),
				HttpStatus.OK);

	}

	
    @PostMapping("/sendNotification")
    public ResponseEntity<ResponseData<String>> sendNotification(@RequestBody GateKeepRequest fcmRequest) {
    	String fcmToken=null;
    	if("PENDING".equalsIgnoreCase(fcmRequest.getStatus())) {
    		Users user = userRepository.findByFlatsFlatNoAndRelationship(fcmRequest.getFlatNo(),Relationships.SELF);
    		if(user == null) {
    			return new ResponseEntity<>(
        				new ResponseData<>("Invalid Flat No", HttpStatus.BAD_REQUEST.value(),null),
        				HttpStatus.BAD_REQUEST);
    		}
    		fcmToken = user.getFcmToken();
    	} else {
    		Optional<GateKeepRequest> existingfcmRequest=gateKeepRequestRepository.findById(fcmRequest.getId());
    		if(!existingfcmRequest.isPresent()) {
    			return new ResponseEntity<>(
        				new ResponseData<>("Invalid gateKeepRequest Id", HttpStatus.BAD_REQUEST.value(),null),
        				HttpStatus.BAD_REQUEST);
    		}
    		Optional<Users> guard = userRepository.findById(fcmRequest.getGuardId());
    		if(!guard.isPresent()) {
    			return new ResponseEntity<>(
        				new ResponseData<>("Invalid Guard Id", HttpStatus.BAD_REQUEST.value(),null),
        				HttpStatus.BAD_REQUEST);
    		}
    		fcmToken = guard.get().getFcmToken();
    		fcmRequest.setGkReqInitTime(existingfcmRequest.get().getGkReqInitTime());
    		
    	}
    	GateKeepRequest request = gateKeepRequestRepository.save(fcmRequest);
        Message message = Message.builder()
                .setNotification(Notification.builder().setTitle(fcmRequest.getTitle()).setBody(fcmRequest.getBody()).build())
                .putData("id", String.valueOf(request.getId()))
                .putData("guardId", request.getGuardId())
                .putData("guardName", request.getGuardName())
                .putData("title", fcmRequest.getTitle())
                .putData("body", fcmRequest.getBody())
                .putData("status", request.getStatus())
                .putData("flatNo", request.getFlatNo())
                .putData("visitorName", request.getVisitorName())
                .putData("visitPurpose", request.getVisitPurpose())
                .putData("path", request.getPath())
                .setToken(fcmToken)
                .build();
        try {
            String response = firebaseMessaging.send(message);
            System.out.println(response);
            return new ResponseEntity<>(
    				new ResponseData<>("Notification sent successfully", HttpStatus.OK.value(), response),
    				HttpStatus.OK);
        } catch (FirebaseMessagingException e) {
            return new ResponseEntity<>(
    				new ResponseData<>("Error in sending Notification", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()),
    				HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
