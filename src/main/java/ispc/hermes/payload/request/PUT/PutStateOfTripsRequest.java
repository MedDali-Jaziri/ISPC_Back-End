package ispc.hermes.payload.request.PUT;

import lombok.Data;

@Data
public class PutStateOfTripsRequest {
    private String nameLocationTrip;

    private Long nameLocationTripId;

    private Boolean isPublishedToHerMeS;
}
