package uz.nammqi.app_demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.nammqi.app_demo.model.Application;
import uz.nammqi.app_demo.model.CombinedData;
import uz.nammqi.app_demo.model.User;
import uz.nammqi.app_demo.model.UsersApps;
import uz.nammqi.app_demo.payload.ApplicationDto;
import uz.nammqi.app_demo.payload.UserRegistrationDto;
import uz.nammqi.app_demo.repository.UserRepository;
import uz.nammqi.app_demo.repository.UsersAppsRepository;
import uz.nammqi.app_demo.service.AppService;
import uz.nammqi.app_demo.service.UserServiceImpl;

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



    @GetMapping("/apps")
    public String viewHomePage(Model model){
        return findPaginated(1,"faculty","asc",model);
    }


    @PostMapping("/saveApp")
    public String saveApplication(@ModelAttribute("app") Application application){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userServiceImpl.getUser(username);
        application.setUserId(user.getId());
        appService.saveApps(application);
        if (Objects.equals(user.getEmail(), "admin"))
            return "redirect:/apps";

        return "redirect:/apply";
    }

    @GetMapping("/showNewAppForm")
    public String showNewEmployeeForm(Model model){
        Application application=new Application();
        model.addAttribute("app",application);
        return "new_app";
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
    public String showUserApp( Model model, Application applications){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userRepository.findByEmail(username);

        Application application=appService.getApplicationByUserId(user.getId());
        if(application==null)
            return "empty";
        List<Application> all = List.of(application);
        model.addAttribute("userApp",all);
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

    @GetMapping("/charts")
    public String charts(){
        return "charts";
    }
    @GetMapping("/cards")
    public String cards(){
        return "cards";
    }

    @GetMapping("/buttons")
    public String button(){
        return "buttons";
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
}