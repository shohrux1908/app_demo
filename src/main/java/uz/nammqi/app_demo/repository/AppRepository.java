package uz.nammqi.app_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.nammqi.app_demo.model.Application;
import uz.nammqi.app_demo.model.User;

import java.util.Optional;

@Repository
public interface AppRepository extends JpaRepository<Application,Long> {

//    Application getAppByUserId(Long user_id);
    Application getApplicationByUserId(Long userId);




}
