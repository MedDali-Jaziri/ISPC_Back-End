package ispc.hermes.payload.response.TouristResponse.GET;

import ispc.hermes.model.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class GetListOfTripsResponse {
    private String message;
    private Set<Trip> data;
}
