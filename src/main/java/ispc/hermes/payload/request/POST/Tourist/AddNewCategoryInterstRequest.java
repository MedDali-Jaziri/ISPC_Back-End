package ispc.hermes.payload.request.POST.Tourist;

import lombok.Data;

@Data
public class AddNewCategoryInterstRequest {
    private Long poiId;
    private String nameCategory;
}
