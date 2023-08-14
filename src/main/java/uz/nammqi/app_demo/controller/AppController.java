package uz.nammqi.app_demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.nammqi.app_demo.model.*;
import uz.nammqi.app_demo.payload.ApplicationDto;
import uz.nammqi.app_demo.payload.UserRegistrationDto;
import uz.nammqi.app_demo.repository.ImageRepository;
import uz.nammqi.app_demo.repository.UserRepository;
import uz.nammqi.app_demo.repository.UsersAppsRepository;
import uz.nammqi.app_demo.service.AppService;
import uz.nammqi.app_demo.service.UserServiceImpl;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class AppController {

    @Autowired
    private AppService appService;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    UsersAppsRepository usersAppsRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;



    @GetMapping("/apps")
    public String viewHomePage(Model model){
        return findPaginated(2,"faculty","asc",model);
    }


    @PostMapping("/saveApp")
    public String saveApplication(@ModelAttribute("app") Application application, RedirectAttributes redirectAttributes){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userServiceImpl.getUser(username);
        application.setUserId(user.getId());
        appService.saveApps(application);

        // Check if the user's email is "admin"
        if (Objects.equals(user.getEmail(), "admin")) {
            redirectAttributes.addAttribute("success", "true");
            return "redirect:/apps";
        }

        redirectAttributes.addAttribute("success", "true");
        return "redirect:/showNewAppForm";
    }


    @GetMapping("/showNewAppForm")
    public String showNewEmployeeForm(Model model){
        Application application=new Application();
        model.addAttribute("app",application);
        return "usersApplies";
    }


    //   admin uchun yol===============
    @PostMapping("/saveAppl")
    public String saveAppl(@ModelAttribute("app") Application application){
        UsersApps usersApps = usersAppsRepository.getByAppId(application.getId());
        application.setUserId(usersApps.getUserId());
        if (application.getBedroomFloor()!=null && application.getBedroomNumber()!=null && application.getRoomNumber()!=null)
            application.setStatus("berildi");
        appService.saveApps(application);
        return "redirect:/apps";
    }


    @GetMapping("/apply")
    public String apply(){
        return "userpage";
    }


    @GetMapping("/basic")
    public String home() {
        return "index";
    }



    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable (value = "id") long id, Model model){
        Application application=appService.getAppById(id);
        Long userId = application.getUserId();
        UsersApps usersApps=new UsersApps(userId,application.getId());
        usersAppsRepository.save(usersApps);
        model.addAttribute("app",application);
        return "update_app";
    }

//=====================================changing
@GetMapping("/showUserApp")
public String showUserApp(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();
    String username = ((UserDetails) principal).getUsername();
    User user = userRepository.findByEmail(username);

    List<Application> userApplications = appService.getApplicationsByUserId(user.getId());

    if (userApplications.isEmpty()) {
        // Foydalanuvchi uchun aplikatsiya mavjud emas, ma'lumotlar bazasida o'rnini tekshirib chiqing
        model.addAttribute("noApplications", true);
    } else {
        model.addAttribute("userApp", userApplications);
    }

    return "user_app";
}



    @GetMapping("/user/info")
    public String userInfo( Model model){
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        Object principal1 = authentication1.getPrincipal();
        String username = ((UserDetails) principal1).getUsername();
        User user = userRepository.findByEmail(username);

        if(user==null)
            return "example";

        Long uId = user.getId();

//        User user = userService.get(id);
        model.addAttribute("user", user);

        return "userinfo";
    }
    @GetMapping("/deleteApp/{id}")
    public String deleteApplication(@PathVariable(value = "id") long id){
        appService.deleteApplicationById(id);
        return "redirect:/apps";
    }



    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model ){
        int pageSize=5;

        Page<Application> page=appService.findPaginated(pageNo,pageSize,sortField,sortDir);
        List<Application> listApplications = page.getContent();

        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reverseSortDir",sortDir.equals("asc")?"desc":"asc");

        model.addAttribute("listApplications", listApplications);
        return "adminpage";

    }

    //rad etish uchun yozyapman
    @GetMapping("/rejectApp/{id}")
    public String reject(@PathVariable (value = "id") long id, Model model){
        Application application=appService.getAppById(id);

        Long userId = application.getUserId();
        UsersApps usersApps=new UsersApps(userId,application.getId());
        usersAppsRepository.save(usersApps);
        application.setStatus("rad etildi");
        application.setBedroomNumber("");
        application.setBedroomFloor("");
        application.setRoomNumber("");
        appService.saveApps(application);
        model.addAttribute("app",application);
        return "redirect:/apps";
    }

    @GetMapping("/user/fac")
    public String userFic( Model model){
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        Object principal1 = authentication1.getPrincipal();
        String username = ((UserDetails) principal1).getUsername();
        User user = userRepository.findByEmail(username);

        if(user==null)
            return "example";

        Long uId = user.getId();
         List<Images>list = imageRepository.findAll();
         model.addAttribute("list",list);

//        User user = userService.get(id);
        return "facility";
    }

    //imageUpload

    @PostMapping("/imageUpload")
    public String imageUpload(@RequestParam("img") MultipartFile img, @RequestParam("facility") String facility, HttpSession session, Model model) {
        try {
            Images images = new Images();
            images.setImageName(img.getOriginalFilename());
            images.setFacility(facility); // Set the provided facility value

            Images uploadImg = imageRepository.save(images);

            if (uploadImg != null) {
                File saveDirectory = new File("static/ImgUpload");
                if (!saveDirectory.exists()) {
                    saveDirectory.mkdirs();
                }

                Path path = Paths.get(saveDirectory.getAbsolutePath() + File.separator + img.getOriginalFilename());
                Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                session.setAttribute("msg", "Imtiyoz muvaffaqiyatli yuklandi");
            } else {
                session.setAttribute("msg", "Imtiyoz yuklashda xatolik yuz berdi");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msg", "Xatolik yuz berdi: " + e.getMessage());
        }

        return "facility"; // Change this to your appropriate view name
    }


    @GetMapping("/users/table")
    public String showUser( Model model){
        List<UserRegistrationDto> users = userServiceImpl.findAllUsers();
        List<ApplicationDto> applications = userServiceImpl.findAllApplication();


        List<CombinedData> combinedDataList = new ArrayList<>();
        int maxSize = Math.max(users.size(), applications.size());
        for (int i = 0; i < maxSize; i++) {
            CombinedData combinedData = new CombinedData();
            if (i < users.size()) {
                combinedData.setUser(users.get(i));
            }
            if (i < applications.size()) {
                combinedData.setApplication(applications.get(i));
            }
            combinedDataList.add(combinedData);
        }
//        model.addAttribute("users",users);
        model.addAttribute("combinedDataList", combinedDataList);
        return "tables";
    }

    //check email
    @GetMapping("/check-email/{email}")
    public ResponseEntity<String> checkEmail(@PathVariable String email) {
        boolean emailExistsInDatabase = checkIfEmailExistsInDatabase(email);

        if (emailExistsInDatabase) {
            return new ResponseEntity<>("Email mavjud", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Email mavjud emas", HttpStatus.NOT_FOUND);
        }
    }

    private boolean checkIfEmailExistsInDatabase(String email) {
        // Bu joyda ma'lumotlar bazasiga ulanishni tekshiruvchi kod bo'lishi kerak
        // Misol uchun, sizning ma'lumotlar bazangizga tegishli so'rovni yuborishingiz mumkin
        // Va bazadan natija olishingiz mumkin, agar email manzili mavjud bo'lsa
        // Ushbu funksiyani tegishli tarzda implement qilishingiz lozim
        return false; // Sizning ma'lumotlar bazangizga qarab javobni belgilang
    }

}
