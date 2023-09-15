package ispc.hermes.payload.response.TouristResponse.POST;

import ispc.hermes.model.UserInterest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddNewUserInterestResponse {
    private String message;
    private UserInterest data;
}
