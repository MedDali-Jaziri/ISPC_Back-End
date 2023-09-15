package ispc.hermes.payload.response.ExpertResponse.GET;

import ispc.hermes.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetListOfCategoriesResponse {
    private String message;
    private List<Category> data;
}
