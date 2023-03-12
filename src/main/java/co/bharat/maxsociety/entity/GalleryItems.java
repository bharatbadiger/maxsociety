package co.bharat.maxsociety.entity;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import co.bharat.maxsociety.enums.GalleryItemType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "galleryItems")
@Data
@AllArgsConstructor
public class GalleryItems {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long galleryItemId;

	@Size(max = 100)
	private String galleryItemName;

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

	private GalleryItemType galleryItemType;
	
	private String galleryItemPath;
	
	@Embedded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "societyCode", referencedColumnName = "societyCode")
	private Society society;
	
	public GalleryItems() {
		this.society= new Society();
	}
	
	public Long getSociety() {
		return society.getSocietyCode();
	}
	
}
