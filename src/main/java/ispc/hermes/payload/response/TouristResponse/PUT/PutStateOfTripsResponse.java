package ispc.hermes.payload.response.TouristResponse.PUT;

import ispc.hermes.model.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PutStateOfTripsResponse {
    private String message;
    private Trip data;
}
