package ispc.hermes.payload.request.POST;

import lombok.Data;

@Data
public class AddPoIToFavoriteTripRequest {
    private String nameLocationTrip;

    private Boolean isFavoriteTrip;

    private Long poiId;
}
