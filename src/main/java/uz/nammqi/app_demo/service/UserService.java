package uz.nammqi.app_demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import uz.nammqi.app_demo.model.User;
import uz.nammqi.app_demo.payload.ApplicationDto;
import uz.nammqi.app_demo.payload.UserRegistrationDto;

import java.util.List;

public interface UserService extends UserDetailsService {

    User save(UserRegistrationDto registrationDto);

    List<UserRegistrationDto> findAllUsers();
    List<ApplicationDto> findAllApplication();
}
