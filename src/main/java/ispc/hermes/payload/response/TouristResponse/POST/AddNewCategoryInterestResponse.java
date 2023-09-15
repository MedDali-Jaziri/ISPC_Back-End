package ispc.hermes.payload.response.TouristResponse.POST;

import ispc.hermes.model.UserCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddNewCategoryInterestResponse {
    private String message;
    private UserCategory data;
}
