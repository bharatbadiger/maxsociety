package co.bharat.maxsociety.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import co.bharat.maxsociety.enums.CircularStatus;
import co.bharat.maxsociety.enums.CircularType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "circulars")
@Data
@AllArgsConstructor
public class Circulars {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long circularId;

	@NotBlank
	@Size(max = 20)
	private String circularNo;

	@Size(max = 100)
	private String subject;

	@Size(max = 2000)
	private String circularText;

	@Size(max = 20)
	private String fileType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "createdBy")
	private Users createdBy;

	/*
	 * public String getCreatedBy() { return createdBy.getUserId(); }
	 */	

	//@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date createdOn;

    @PrePersist
    protected void onCreate() {
    	createdOn = new Date();
    	updatedOn = createdOn;
    }

    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "updatedBy")
	private Users updatedBy;

	/*
	 * public String getUpdatedBy() { return updatedBy.getUserId(); }
	 */

	private Date updatedOn;
    @PreUpdate
    protected void onUpdate() {
    	updatedOn = new Date();
    }

	private CircularType circularType;
	
	private CircularStatus circularStatus;
	
	private String circularCategory; 
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "societyCode")
    private Society society;
    
    public Circulars() {
    	this.society = new Society();
    }
    public Long getSociety() {
    	return society.getSocietyCode();
    }
    
	@OneToMany(mappedBy = "circular", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CircularImage> circularImages;
	
	private Date eventDate;
	
	private boolean showEventDate;
}
