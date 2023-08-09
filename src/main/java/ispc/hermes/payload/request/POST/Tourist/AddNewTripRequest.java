package ispc.hermes.payload.request.POST.Tourist;

import lombok.Data;

@Data
public class AddNewTripRequest {
    private String descriptionBrief;
    private String nameLocationTrip;
    private String nameLocationTripUpdate;
    private Boolean isPublishedToHerMeS;
}
