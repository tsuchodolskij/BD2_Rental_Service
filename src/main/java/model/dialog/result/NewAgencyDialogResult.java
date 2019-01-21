package model.dialog.result;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
public class NewAgencyDialogResult {

    @NotNull
    private final String code;

    @NotNull
    private final String house;

    @NotNull
    private final String street;

    @NotNull
    private final Long parkingPlaces;
}
