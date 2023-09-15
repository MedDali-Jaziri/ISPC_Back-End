package ispc.hermes.payload.response.AdminResponse.GET;

import ispc.hermes.model.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class GetListOfTripsUsingAdminAccountResponse {
    private String message;
    private Set<Trip> data;
}
