package uz.nammqi.app_demo.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phoneNumber;

}
