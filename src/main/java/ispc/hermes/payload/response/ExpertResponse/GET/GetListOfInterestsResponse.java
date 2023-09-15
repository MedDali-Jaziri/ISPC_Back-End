package ispc.hermes.payload.response.ExpertResponse.GET;

import ispc.hermes.model.Interest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetListOfInterestsResponse {
    private String message;
    private List<Interest> data;
}
