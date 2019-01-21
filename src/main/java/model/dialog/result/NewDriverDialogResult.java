package model.dialog.result;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class NewDriverDialogResult {

    @NotNull
    private final String driverLicenceId;

    @NotNull
    private final String familyName;

    @NotNull
    private final String busRegistrationNumber;

    @NotNull
    private final String districtName;
}
