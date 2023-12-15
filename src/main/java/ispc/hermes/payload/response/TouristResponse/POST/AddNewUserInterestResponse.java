package ispc.hermes.payload.response.TouristResponse.POST;

import ispc.hermes.model.UserTopic;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AddNewUserInterestResponse {
    private String message;
    private List<UserTopic> data;

    public AddNewUserInterestResponse(String message){
        this.message = message;
    }
}
