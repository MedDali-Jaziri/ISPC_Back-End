package ispc.hermes.payload.response.TouristResponse.POST;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopicRequest {
    @JsonProperty("id")
    private String id;

    @JsonProperty("label")
    private String label;
}
