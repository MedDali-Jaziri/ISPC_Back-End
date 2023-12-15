package ispc.hermes.payload.request.POST.Tourist;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class SetPoIToHAIRequest {
    private Long setPoIHAIId;

    private Set<String> labelOfTopics;

}
