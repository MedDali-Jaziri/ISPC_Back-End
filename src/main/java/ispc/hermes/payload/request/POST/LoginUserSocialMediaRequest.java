package ispc.hermes.payload.request.POST;

import lombok.Data;

@Data
public class LoginUserSocialMediaRequest {
    private String email;
    private String username;
}
