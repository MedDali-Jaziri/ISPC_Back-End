package ispc.hermes.payload.request.POST.Tourist;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddUserSocialMediaRequest {
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    @NotBlank
    @Size(min = 3, max = 20)
    private String userName;

    private Boolean isConnectedWithFBAccount;

    private Boolean isConnectedWithInstAccount;
}
