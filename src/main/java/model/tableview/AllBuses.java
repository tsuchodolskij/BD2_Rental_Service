package model.tableview;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
public class AllBuses {

    private final String registration;
    private final Long capacity;
    private final Date evaluationDate;
    private final String district;
}
