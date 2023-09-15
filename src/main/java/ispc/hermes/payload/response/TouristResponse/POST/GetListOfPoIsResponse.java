package ispc.hermes.payload.response.TouristResponse.POST;

import ispc.hermes.model.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class GetListOfPoIsResponse {
    private String message;
    private Set<Trip> data;
}
