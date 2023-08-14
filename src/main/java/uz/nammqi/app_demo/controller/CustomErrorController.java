package uz.nammqi.app_demo.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(Model model) {
        model.addAttribute("errorMessage", "Bu sahifaga kirishda xatolik yuz berdi.");
        return "404"; // "error.html" sahifasini qaytaradi
    }

}
