package model.dialog.result;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class NewBusDialogResult {

    @NotNull
    private final String registrationNumber;

    @NotNull
    private final Date techEvaluationDate;

    @NotNull
    private final Long sizeCapacity;

    @NotNull
    private final Long ocAc;
}
