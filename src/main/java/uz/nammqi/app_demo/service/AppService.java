package uz.nammqi.app_demo.service;

import org.springframework.data.domain.Page;
import uz.nammqi.app_demo.model.Application;

import java.util.List;

public interface AppService {
    List<Application> getAllApps();

    //admin uchun==========


    void saveApps(Application application);

    Application getAppById(long id);


    //=========changing
    Application getApplicationByUserId(Long userId);

    //==============
    void deleteApplicationById(long id);

    Page<Application> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    List<Application> getApplicationsByUserId(Long id);
}
