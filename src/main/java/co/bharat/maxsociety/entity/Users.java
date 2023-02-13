package co.bharat.maxsociety.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import co.bharat.maxsociety.enums.Gender;
import co.bharat.maxsociety.enums.Relationships;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {
	@Id
	private String userId;

	@NotBlank
	@Size(max = 60)
	private String userName;

	
	@Column(length = 20)
	private Relationships relationship;
	
	@NotBlank
	@Size(max = 14)
	private String mobileNo;
	
	@Column(length = 20)
	private Gender gender;
	
	private Date dob;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

    @OneToOne
    @JoinColumn(name = "flatNo", referencedColumnName = "flatNo")
	//@JoinTable(name = "flats", joinColumns = @JoinColumn(name = "flatNo"), inverseJoinColumns = @JoinColumn(name = "flatNo"))
	private Flats flats;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	@Builder.Default
	private Set<Role> roles = new HashSet<>();
	
	@Size(max = 200)
	private String imagePath;
	
}
