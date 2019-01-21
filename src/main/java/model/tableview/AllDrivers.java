package model.tableview;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
public class AllDrivers {

    private final String driversLicenseID;
    private final String familyName;
    private final String busRegistrationNumber;
    private final String districtName;
}
