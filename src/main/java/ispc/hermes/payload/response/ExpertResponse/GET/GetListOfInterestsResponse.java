package ispc.hermes.payload.response.ExpertResponse.GET;

import ispc.hermes.model.TopicHAI;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetListOfInterestsResponse {
    private String message;
    private List<TopicHAI> data;
}
