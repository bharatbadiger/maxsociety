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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flatNo", referencedColumnName = "flatNo")
	//@JoinTable(name = "flats", joinColumns = @JoinColumn(name = "flatNo"), inverseJoinColumns = @JoinColumn(name = "flatNo"))
	private Flats flats;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	@Builder.Default
	private Set<Role> roles = new HashSet<>();
	
	@Size(max = 200)
	private String imagePath;
	
	private Date createdOn;

    @PrePersist
    protected void onCreate() {
    	createdOn = new Date();
    	updatedOn = createdOn;
    }

	private Date updatedOn;
	
    @PreUpdate
    protected void onUpdate() {
    	updatedOn = new Date();
    }
	
    private String designation;
    
    private String category;
    
	
	@Transient
	private long familyMembersCount;

	public long getFamilyMembersCount() {
		if(this.flats == null) {
			return 0;
		}
		return this.flats.getUsers().size() - 1;
	}
	/*
	 * @Transient private List<Users> familyMembers;
	 * 
	 * public List<Users> getFamilyMembers() { List<Users> usersList =
	 * this.flats.getUsers(); usersList.removeIf(u -> u.getUserId().equals(userId));
	 * return usersList; }
	 */
	 

	
	


}
