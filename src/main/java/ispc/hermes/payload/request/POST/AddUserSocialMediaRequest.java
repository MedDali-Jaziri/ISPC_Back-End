package ispc.hermes.payload.request.POST;

import lombok.Data;

@Data
public class AddUserSocialMediaRequest {
    private String email;
    private String userName;
    private Boolean isConnectedWithFBAccount;
    private Boolean isConnectedWithInstAccount;
}
