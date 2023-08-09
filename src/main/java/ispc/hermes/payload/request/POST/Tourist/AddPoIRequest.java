package ispc.hermes.payload.request.POST.Tourist;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data

public class AddPoIRequest {
    private String descriptionPoI;
    private MultipartFile imagePoI;
    private Double longitudeLocationPoI;
    private Double latitudeLocationPoI;
    private String nameLocationPoI;
    private String hashtagsPoI;
    private MultipartFile audioPoI;
    private Boolean isPublishedToFB;
    private Boolean isPublishedToInsta;
    private Boolean isPersonalPoI;
}
