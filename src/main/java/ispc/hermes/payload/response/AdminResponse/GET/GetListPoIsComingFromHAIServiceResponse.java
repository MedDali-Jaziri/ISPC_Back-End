package ispc.hermes.payload.response.AdminResponse.GET;

import ispc.hermes.model.PoI;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetListPoIsComingFromHAIServiceResponse {
    private String message;
    private List<PoI> data;

    public GetListPoIsComingFromHAIServiceResponse(String message){
        this.message = message;
    }
}
