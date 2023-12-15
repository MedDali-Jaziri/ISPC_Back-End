package ispc.hermes.payload.response.TouristResponse.POST;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SetHAIRequest {
    @JsonProperty("userID")
    private String userId;

    @JsonProperty("duration")
    private int duration;

    @JsonProperty("userLocation")
    private List<Double> userLocation;

    @JsonProperty("groupSize")
    private int groupSize;

    @JsonProperty("dVector")
    private List<Boolean> dVector;

    @JsonProperty("mVector")
    private List<Boolean> mVector;

    @JsonProperty("topics")
    private List<TopicRequest> topics;
}

