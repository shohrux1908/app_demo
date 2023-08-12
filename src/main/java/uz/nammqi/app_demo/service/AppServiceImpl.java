package uz.nammqi.app_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.nammqi.app_demo.model.Application;
import uz.nammqi.app_demo.repository.AppRepository;
import uz.nammqi.app_demo.repository.ApplicationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private AppRepository appRepository;
    private final ApplicationRepository applicationRepository;

    public AppServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public List<Application> getApplicationsByUserId(Long userId) {
        return applicationRepository.findByUserId(userId);
    }

    @Override
    public List<Application> getAllApps() {
        return appRepository.findAll();
    }

    @Override
    public void saveApps(Application application) {
        this.appRepository.save(application);
    }

    @Override
    public Application getAppById(long id) {
        Optional<Application> optionalApplication = appRepository.findById(id);
        Application application = null;
        if (optionalApplication.isPresent())
            application = optionalApplication.get();
        else
            throw new RuntimeException("Application not found for id :: " + id);
        return application;
    }


    //============changing
    @Override
    public Application getApplicationByUserId(Long userId) {
        Application application = appRepository.getApplicationByUserId(userId);
        return application;
    }

    @Override
    public void deleteApplicationById(long id) {
        this.appRepository.deleteById(id);
    }

    @Override
    public Page<Application> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.appRepository.findAll(pageable);
    }


}

