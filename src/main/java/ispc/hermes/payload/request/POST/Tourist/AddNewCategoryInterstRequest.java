package ispc.hermes.payload.request.POST.Tourist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddNewCategoryInterstRequest {
    private String nameCategory;
    private Long poiId;
}
