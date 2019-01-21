package model.dialog.result;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RegistrationDialogResult {

    @NotNull
    private final String username;

    @NotNull
    private final String password;

    @NotNull
    private final String phoneNumber;

}
