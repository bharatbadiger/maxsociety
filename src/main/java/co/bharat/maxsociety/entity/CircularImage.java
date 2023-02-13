package co.bharat.maxsociety.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "circular_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CircularImage {
	
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;
    
    public CircularImage(String imageUrl) {
        this.imageUrl = imageUrl;
      }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "circularId")
    private Circulars circular;

    public String getCircular() {
        return circular.getCircularId();
    }
    
    
}
