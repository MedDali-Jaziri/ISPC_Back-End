package ispc.hermes.payload.response.TouristResponse.POST;

import ispc.hermes.model.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class AddAnPoIToATripResponse {
    private String message;
    private Optional<Trip> trip;
}
