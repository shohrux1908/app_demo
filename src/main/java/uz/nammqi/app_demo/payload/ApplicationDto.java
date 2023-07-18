package uz.nammqi.app_demo.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDto {

    private String faculty;


    private String groups;


    private String region;


    private String district;


    private String village;

    private String bedroomNumber;

    private String bedroomFloor;

    private String roomNumber;


    private String passportSerialNumber;

    private String status;


    private Long userId;
}
