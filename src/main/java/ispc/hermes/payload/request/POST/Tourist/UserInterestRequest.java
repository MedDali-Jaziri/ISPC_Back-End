package ispc.hermes.payload.request.POST.Tourist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInterestRequest {
    private String nameInterest;
    private Long poiId;
}
