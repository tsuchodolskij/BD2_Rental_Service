package model.tableview;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class AgenciesInDistrict {

    private final Long parkingPlaces;
    private final String code;
    private final String house;
    private final String street;
}
