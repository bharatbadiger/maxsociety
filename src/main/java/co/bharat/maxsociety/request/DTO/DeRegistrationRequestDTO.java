package co.bharat.maxsociety.request.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeRegistrationRequestDTO {
    private String userId;

    private String createdBy;

    private String updatedBy;
    
    private String status;
}
