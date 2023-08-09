package ispc.hermes.payload.request.POST.Tourist;

import lombok.Data;

@Data
public class AddAnPoIToTripRequest {
    private Long poiId;
    private String nameLocationTrip;
}
