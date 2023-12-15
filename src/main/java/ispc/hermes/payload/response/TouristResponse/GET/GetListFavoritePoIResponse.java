package ispc.hermes.payload.response.TouristResponse.GET;

import ispc.hermes.model.PoI;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetListFavoritePoIResponse {
    private String message;
    private List<PoI> data;
}
