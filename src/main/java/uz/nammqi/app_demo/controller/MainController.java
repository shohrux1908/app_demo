package uz.nammqi.app_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import uz.nammqi.app_demo.model.Role;
import uz.nammqi.app_demo.model.User;
import uz.nammqi.app_demo.repository.RoleRepository;
import uz.nammqi.app_demo.repository.UserRepository;
import uz.nammqi.app_demo.service.UserServiceImpl;

import java.util.Collection;

@Controller
public class MainController {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServiceImpl userServiceImpl;

    private static int count=0;


@GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index() {
        count++;
        return count > 1 ? "redirect:/index" : "index";
    }


//

    @GetMapping("/index")
    public String basic() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if(principal.equals("anonymousUser"))
            return "index";
        String username = ((UserDetails) principal).getUsername();
        User user = userServiceImpl.getUser(username);
        if (user==null){
            return "404";
        }
        Collection<Role> roles = user.getRoles();
        User admin = userServiceImpl.getUser("admin");
        Collection<Role> adminRoles = admin.getRoles();
        boolean equals = adminRoles.equals(roles);

        return equals ? "redirect:/apps" : "userpage";
    }

}
