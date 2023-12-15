package ispc.hermes.payload.request.POST.Tourist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTopicRequest {
//    private String label;
    private Set<String> labelOfTopics;
    private Long poiId;
}
