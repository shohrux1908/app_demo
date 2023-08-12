package uz.nammqi.app_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.nammqi.app_demo.model.Images;

public interface ImageRepository extends JpaRepository<Images,Integer> {
}
