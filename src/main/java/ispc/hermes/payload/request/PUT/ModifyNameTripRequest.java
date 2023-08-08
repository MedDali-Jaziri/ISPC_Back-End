package ispc.hermes.payload.request.PUT;

import lombok.Data;

@Data
public class ModifyNameTripRequest {
    private String nameLocationTrip;

    private String nameLocationTripUpdated;
}
