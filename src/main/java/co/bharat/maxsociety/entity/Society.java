package co.bharat.maxsociety.entity;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "society")
@Data
//@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Society {

	public Society() {
		this.societyCode = 1l;
	}
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long societyCode;
	
	public void setSocietyCode(Long societyCode) {
		this.societyCode = societyCode!=null?societyCode:1;		
	}

	@Embedded
	private SocietyDetails societyDetails;

	@Embedded
	//@AttributeOverrides(value = { @AttributeOverride(name = "addressLine1", column = @Column(name = "house_number")),
	//		@AttributeOverride(name = "addressLine2", column = @Column(name = "street")) })
	private Address address;
	
}
