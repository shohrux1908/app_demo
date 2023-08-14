package uz.nammqi.app_demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String faculty;

    @Column(nullable = false,name = "groups")
    private String groups;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String village;

    private String bedroomNumber;

    private String bedroomFloor;

    private String roomNumber;

    private String level;

    @Column(nullable = false)
    private String passportSerialNumber;

    private String status;


    private Long userId;

    public Application(Long id, String faculty, String groups, String region, String district, String village, String bedroomNumber, String bedroomFloor, String roomNumber, String level, String passportSerialNumber, String status, Long userId) {
        this.id = id;
        this.faculty = faculty;
        this.groups = groups;
        this.region = region;
        this.district = district;
        this.village = village;
        this.bedroomNumber = bedroomNumber;
        this.bedroomFloor = bedroomFloor;
        this.roomNumber = roomNumber;
        this.level = level;
        this.passportSerialNumber = passportSerialNumber;
        this.status = status;
        this.userId = userId;
    }
}
