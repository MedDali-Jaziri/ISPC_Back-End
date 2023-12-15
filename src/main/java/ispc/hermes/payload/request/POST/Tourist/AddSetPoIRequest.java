package ispc.hermes.payload.request.POST.Tourist;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data

public class AddSetPoIRequest {
    private Integer duration;

    @Size(min = 2, max = 3)
    private List<Double> userLocation;

    private Integer groupSize;

    @Size(min = 4, max = 4)
    private List<Boolean> disabilityVector;

    @Size(min = 4, max = 4)
    private List<Boolean> mobilityVector;
}
