package uz.nammqi.app_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.nammqi.app_demo.model.Application;

import java.util.List;

public interface ApplicationRepository  extends JpaRepository<Application, Long> {
    List<Application> findByUserId(Long userId);
}
