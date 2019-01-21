package model.dialog.result;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ChangeBusAgencyDialogResult {

    @NotNull
    private final String DistrictName;

    @NotNull
    private final Long AgencyId;
}
