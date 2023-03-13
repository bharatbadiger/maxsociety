package co.bharat.maxsociety.entity;

import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "Flats")
@Data
//@NoArgsConstructor
@AllArgsConstructor
//@IdClass(FlatsId.class)
public class Flats {

	@Id
	private String flatNo;

	@Embedded
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "societyCode", referencedColumnName = "societyCode")
	// @JoinTable(name = "society", joinColumns = @JoinColumn(name = "societyCode"),
	// inverseJoinColumns = @JoinColumn(name = "societyCode"), referencedColumnName
	// = "societyCode")
	private Society society;

	public Flats() {
		this.society = new Society();
	}

	public Long getSociety() {
		return society.getSocietyCode();
	}

	@NotBlank
	@Size(max = 20)
	private String wing;

	@Size(max = 20)
	private String tower;

	private Long floor;

	@Size(max = 20)
	private String buitlUpArea;

	@Size(max = 20)
	private String carpetArea;

	/*
	 * @OneToMany(mappedBy = "vehiclesAndFlats") private List<Vehicles> vehicles;
	 */

	@JsonIgnore
	@OneToMany(mappedBy = "flats")
	private List<Users> users;

	/*
	 * public int getNumUsers() { return users == null ? 0 : users.size(); }
	 */

	@OneToMany(mappedBy = "flats")
	private List<Vehicles> vehicles;

	/*
	 * public List<Users> getAllUsersAndVehicles() { List<Vehicles> flatVehicles =
	 * this.getVehicles(); // Add all the users' vehicles to the flatVehicles list
	 * for (Users user : flatUsers) { flatVehicles.addAll(user.getVehicles()); }
	 * 
	 * return flatUsers; }
	 */

}
