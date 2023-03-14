package co.bharat.maxsociety.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "emergency_contacts")
@Data
public class EmergencyContacts {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Column(unique=true)
    @NotBlank
    private String category;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "societyCode")
    private Society society;
    
    public EmergencyContacts() {
    	this.society = new Society();
    }
    
    public Long getSociety() {
    	return society.getSocietyCode();
    }
    
    @ElementCollection
    @CollectionTable(name = "emergency_contact_phones", joinColumns = @JoinColumn(name = "emergency_contact_id"))
    @Column(name = "phone_number")
    private Set<String> phoneNumbers = new HashSet<>();
}

