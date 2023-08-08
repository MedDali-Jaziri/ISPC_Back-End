package ispc.hermes.payload.request.POST;

import lombok.Data;

@Data
public class AddNewTripRequest {
    private String descriptionBrief;
    private String nameLocationTrip;
    private String nameLocationTripUpdate;
    private Boolean isPublishedToHerMeS;
}
