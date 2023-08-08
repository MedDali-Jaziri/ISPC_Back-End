package ispc.hermes.payload.request.POST;

import lombok.Data;

@Data
public class AddAnPoIToTripRequest {
    private Long poiId;
    private String nameLocationTrip;
}
