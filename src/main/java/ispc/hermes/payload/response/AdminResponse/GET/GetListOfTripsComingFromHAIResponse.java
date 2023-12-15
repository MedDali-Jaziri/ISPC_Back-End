package ispc.hermes.payload.response.AdminResponse.GET;

import ispc.hermes.model.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetListOfTripsComingFromHAIResponse {
    private String message;
    private List<Trip> data;

    public GetListOfTripsComingFromHAIResponse(String message){
        this.message = message;
    }
}
