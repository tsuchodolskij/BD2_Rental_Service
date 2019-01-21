package model.dialog.result;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
public class FinishHireDialogResult {

    @NotNull
    private final Timestamp rentFinishDate;

    @NotNull
    private final String DistrictName;

    @NotNull
    private final Long StationId;

    @NotNull
    private final Long TerminalId;
}
