package ispc.hermes.payload.response.TouristResponse.POST;

import ispc.hermes.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;
import java.util.Set;


@Data
@AllArgsConstructor
public class AddFavoritePoIOfHAIResponse {
    private String message;
    private User data;
}
