package ispc.hermes.payload.response.TouristResponse.POST;

import ispc.hermes.model.PoI;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class GetAllPoIInEachTripsResponse {
    private String message;
    private Set<PoI> data;
}
