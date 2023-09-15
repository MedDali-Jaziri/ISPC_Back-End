package ispc.hermes.payload.response.TouristResponse.POST;

import ispc.hermes.model.PoI;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddNewPoIResponse {
    private String message;
    private PoI data;
}
