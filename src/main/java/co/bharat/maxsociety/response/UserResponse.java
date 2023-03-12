package co.bharat.maxsociety.response;

import java.util.List;

import co.bharat.maxsociety.entity.Users;
import co.bharat.maxsociety.entity.Vehicles;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private Users user;
    private List<Users> familyMembers;
    private List<Vehicles> vehicles;
}
