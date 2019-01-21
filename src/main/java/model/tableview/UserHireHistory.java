package model.tableview;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class UserHireHistory {

    private final Timestamp startDate;
    private final Timestamp finishDate;
    private final Long hirePrice;
    private final String vehicleType;
}
