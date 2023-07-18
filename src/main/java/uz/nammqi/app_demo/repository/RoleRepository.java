package uz.nammqi.app_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.nammqi.app_demo.model.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {


}
