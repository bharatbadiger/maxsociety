package co.bharat.maxsociety.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicles {
	
	@Id
	private String vehicleNo;
	
	@NotBlank
	@Size(max = 20)
	private String vehicleType;
	
	@Size(max = 20)
	private String brand;
		
	@Size(max = 20)
	private String model;
	
	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "flatNo") private Flats vehiclesAndFlats;
	 */
    
    @ManyToOne(fetch = FetchType.LAZY)
	/*For Composite key
	 * @JoinColumns({
	 * 
	 * @JoinColumn(name = "flatNo", referencedColumnName = "flatNo"),
	 * 
	 * @JoinColumn(name = "societyCode", referencedColumnName = "societyCode") })
	 */
    @JoinColumn(name = "flatNo")
    private Flats flats;
    
	/*
	 * public String getFlats() { return flats.getFlatNo(); }
	 */
	
	/*For Composite key
	 * public String getFlats() { return flats.getId().getFlatNo(); }
	 */
    private String image;
}
