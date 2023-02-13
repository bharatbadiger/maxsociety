package co.bharat.maxsociety.entity;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocietyDetails {
    @NotNull
    @Size(max = 40)
    private String societyName;

    @Size(max = 30)
    private String reraRegNo;

    @Size(max = 20)
    private String societyRegNo;
    
    @Size(max = 10)
    private String wardNo;
    
    private String imagePath;

}
