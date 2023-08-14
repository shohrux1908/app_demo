package uz.nammqi.app_demo.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.nammqi.app_demo.model.Application;
import uz.nammqi.app_demo.model.Role;
import uz.nammqi.app_demo.model.User;
import uz.nammqi.app_demo.repository.AppRepository;
import uz.nammqi.app_demo.repository.RoleRepository;
import uz.nammqi.app_demo.repository.UserRepository;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AppRepository appRepository;



    @Value("${spring.sql.init.mode}")
    private String initialMode;

    @Override
    public void run(String... args){

        if (initialMode.equals("always")){
//            Role admin=new Role("ROLE_ADMIN");
//            roleRepository.save(admin);

//            Role admin=roleRepository.save(new Role("ROLE_ADMIN"));

            userRepository.save(new User(
                    1L,
                    "static/admin",
                    "static/admin",
                    "admin",
                    "",
                    passwordEncoder.encode( "890"),


                    List.of(new Role("ROLE_ADMIN"))




            ));
            appRepository.save(new Application(1L,"","","","","","","","","","","", 1L));




        }




    }
}
