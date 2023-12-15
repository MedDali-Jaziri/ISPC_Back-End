package ispc.hermes.payload.response.ExpertResponse.GET;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data

public class AddTopicsResponse {
    private String message;
    private List<String> data;
    public AddTopicsResponse(String message){
        this.message = message;
    }
}
