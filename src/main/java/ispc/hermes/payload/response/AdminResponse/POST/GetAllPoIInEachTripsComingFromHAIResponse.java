package ispc.hermes.payload.response.AdminResponse.POST;

import ispc.hermes.model.PoI;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetAllPoIInEachTripsComingFromHAIResponse {
    private String message;
    private List<PoI> data;

    public GetAllPoIInEachTripsComingFromHAIResponse(String message){
        this.message = message;
    }
}
