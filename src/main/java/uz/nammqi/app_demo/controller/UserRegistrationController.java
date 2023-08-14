package uz.nammqi.app_demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.nammqi.app_demo.payload.UserRegistrationDto;
import uz.nammqi.app_demo.service.UserService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    private UserService userService;

    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto(){
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new UserRegistrationDto());
        return "registration";
    }

    @PostMapping
    @ResponseBody // Bu qator qo'shing
    public ResponseEntity<Map<String, Object>> registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto){
        userService.save(registrationDto);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        // ... Kerakli boshqa ma'lumotlar ...

        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<String> checkEmail(@PathVariable String email) {
        boolean emailExists = userService.checkIfEmailExists(email);

        if (emailExists) {
            return new ResponseEntity<>("Bu email allaqachon ro'yxatdan o'tgan", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
