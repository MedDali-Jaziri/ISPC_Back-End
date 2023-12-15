package ispc.hermes.payload.response.TouristResponse.POST;

import ispc.hermes.model.SetPoIHAI;
import ispc.hermes.model.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddSetPoIHAIResponse {
    private String message;
    private SetPoIHAI data;
}
