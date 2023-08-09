package ispc.hermes.payload.request.POST.Tourist;

import lombok.Data;

@Data
public class AddPoIToFavoriteTripRequest {
    private String nameLocationTrip;

    private Boolean isFavoriteTrip;

    private Long poiId;
}
