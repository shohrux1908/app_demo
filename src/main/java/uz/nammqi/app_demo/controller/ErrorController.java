package uz.nammqi.app_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.nammqi.app_demo.service.UserService;

@RestController
@RequestMapping("/api")
public class ErrorController {

    private final UserService userService;

    @Autowired
    public ErrorController(UserService userService) {
        this.userService = userService;
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
