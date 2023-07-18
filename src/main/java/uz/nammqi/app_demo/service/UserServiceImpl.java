package uz.nammqi.app_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import uz.nammqi.app_demo.model.Application;
import uz.nammqi.app_demo.model.Role;
import uz.nammqi.app_demo.model.User;
import uz.nammqi.app_demo.payload.ApplicationDto;
import uz.nammqi.app_demo.payload.UserRegistrationDto;
import uz.nammqi.app_demo.repository.ApplicationRepository;
import uz.nammqi.app_demo.repository.UserRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApplicationRepository applicationRepository;


    @Autowired
    BCryptPasswordEncoder passwordEncoder;



    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public User save(UserRegistrationDto registrationDto) {
        User user= new User(registrationDto.getFirstname(), registrationDto.getLastname(),
                registrationDto.getEmail(),  registrationDto.getPhoneNumber(),  passwordEncoder.encode(registrationDto.getPassword()), Arrays.asList(new Role("ROLE_USER")));
        return userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByEmail(username);
        if (user==null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    //====
    public User getUser(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserRegistrationDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    private UserRegistrationDto convertEntityToDto(User user){
        UserRegistrationDto userDto = new UserRegistrationDto();
//        String[] name = user.getName().split(" ");.
        userDto.setId(user.getId());
        userDto.setId(user.getId());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setPassword(user.getPassword());
        return userDto;
    }
    private ApplicationDto converEntytyMessage(Application application){
        ApplicationDto applicationDto=new ApplicationDto();
        applicationDto.setUserId(application.getUserId());
        applicationDto.setFaculty(application.getFaculty());
        applicationDto.setDistrict(application.getDistrict());
        applicationDto.setRegion(application.getRegion());
        applicationDto.setVillage(application.getVillage());
        applicationDto.setGroups(application.getGroups());
        applicationDto.setBedroomNumber(application.getBedroomNumber());
        applicationDto.setBedroomFloor(application.getBedroomFloor());
        applicationDto.setRoomNumber(application.getRoomNumber());
        applicationDto.setStatus(application.getStatus());
        applicationDto.setPassportSerialNumber(application.getPassportSerialNumber());
        return applicationDto;
    }

    @Override
    public List<ApplicationDto> findAllApplication() {
        List<Application> applications = applicationRepository.findAll();
        return  applications.stream().map((application -> converEntytyMessage(application)))
                .collect(Collectors.toList());
    }
}
