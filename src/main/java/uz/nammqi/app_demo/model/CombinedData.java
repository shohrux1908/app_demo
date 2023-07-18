package uz.nammqi.app_demo.model;

import uz.nammqi.app_demo.payload.ApplicationDto;
import uz.nammqi.app_demo.payload.UserRegistrationDto;

public class CombinedData {
    private UserRegistrationDto user;
    private ApplicationDto application;

    // Getter va setter metodlari


    public UserRegistrationDto getUser() {
        return user;
    }

    public void setUser(UserRegistrationDto user) {
        this.user = user;
    }

    public ApplicationDto getApplication() {
        return application;
    }

    public void setApplication(ApplicationDto application) {
        this.application = application;
    }
}