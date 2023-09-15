package ispc.hermes.payload.response.AdminResponse.GET;

import ispc.hermes.model.PoI;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetListPoIsOfTouringClubAddByTheScriptResponse {
    private String message;
    private List<PoI> poIList;
}
