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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.bharat.maxsociety.entity.Vehicles;
import co.bharat.maxsociety.repository.VehiclesRepository;
import co.bharat.maxsociety.response.ResponseData;

@RestController
@RequestMapping("/maxsociety/vehicles")
public class VehiclesController {
	private final VehiclesRepository vehiclesRepository;

	public VehiclesController(VehiclesRepository vehiclesRepository) {
		this.vehiclesRepository = vehiclesRepository;
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
	public ResponseEntity<ResponseData<List<Vehicles>>> getAllVehicles() {
		List<Vehicles> vehicles = vehiclesRepository.findAll();
	    if (vehicles.isEmpty()) {
	        return new ResponseEntity<>(new ResponseData<List<Vehicles>>("No Vehicles Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	    }
		return new ResponseEntity<>(new ResponseData<List<Vehicles>>("All Vehicles Fetched successfully", HttpStatus.OK.value(), vehicles), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public ResponseEntity<ResponseData<Vehicles>>  getVehicleByVehicleNo(@PathVariable String id) {
		Optional<Vehicles> vehicle = vehiclesRepository.findById(id);
		if (!vehicle.isPresent()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
		return new ResponseEntity<>(new ResponseData<Vehicles>("Vehicle Fetched successfully", HttpStatus.OK.value(), vehicle.get()), HttpStatus.OK);
	}

	@GetMapping("/flat/{flatNo}")
    //@RequestParam(value = "page", defaultValue = "0") int page,
    //@RequestParam(value = "size", defaultValue = "10") int size)
	public ResponseEntity<ResponseData<List<Vehicles>>> getVehiclesByFlatNo(@PathVariable String flatNo) {
	    List<Vehicles> vehicles = vehiclesRepository.findByFlats_FlatNo(flatNo);
	    if (vehicles.isEmpty()) {
	        return new ResponseEntity<>(new ResponseData<List<Vehicles>>("Vehicle Not Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	    }
	    
	    return new ResponseEntity<>(new ResponseData<List<Vehicles>>("Vehicle Fetched", HttpStatus.OK.value(), vehicles),HttpStatus.OK);

	    //return new ResponseEntity<>(vehicles, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseData<Vehicles>> createVehicle(@RequestBody Vehicles vehicle) {
		Vehicles newVehicle = vehiclesRepository.save(vehicle);
		return new ResponseEntity<>(new ResponseData<Vehicles>("Vehicle Created Successfully", HttpStatus.OK.value(), newVehicle),HttpStatus.OK);
	}

	@PutMapping(value = {"/", "/{vehicleNo}"})
	public ResponseEntity<ResponseData<Vehicles>> updateVehicle(@PathVariable(required = false) String vehicleNo,  @RequestBody Vehicles vehicle) {
		if(vehicleNo == null) {
			vehicleNo=vehicle.getVehicleNo();
		}
		Optional<Vehicles> existingVehicle = vehiclesRepository.findById(vehicleNo);
		if (!existingVehicle.isPresent()) {
	        return new ResponseEntity<>(new ResponseData<Vehicles>("Vehicle Not Found", HttpStatus.NOT_FOUND.value(), null),HttpStatus.NOT_FOUND);
	        
	    }
		Vehicles updatedVehicle = vehiclesRepository.save(vehicle);
		return new ResponseEntity<>(new ResponseData<Vehicles>("Vehicle Updated Successfully", HttpStatus.OK.value(), updatedVehicle),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteVehicle(@PathVariable("id") String id) {
		vehiclesRepository.deleteById(id);
	    return ResponseEntity.noContent().build();
	}
}