package uz.nammqi.app_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.nammqi.app_demo.model.Application;
import uz.nammqi.app_demo.model.UsersApps;

@Repository
public interface UsersAppsRepository extends JpaRepository<UsersApps,Long> {

    @Query(value = "select * from users_apps where app_id=:appId limit 1",nativeQuery = true)
    UsersApps getByAppId(Long appId);

    Application save(Application application);
}
