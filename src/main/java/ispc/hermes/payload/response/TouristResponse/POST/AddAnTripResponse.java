package ispc.hermes.payload.response.TouristResponse.POST;

import ispc.hermes.model.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddAnTripResponse {
    private String message;
    private Trip data;
}
