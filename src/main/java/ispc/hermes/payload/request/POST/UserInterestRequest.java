package ispc.hermes.payload.request.POST;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInterestRequest {
    private String nameRequest;
    private Integer poiId;
}
