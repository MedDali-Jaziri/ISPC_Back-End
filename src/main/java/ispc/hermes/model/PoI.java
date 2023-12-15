package ispc.hermes.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.util.List;


@Entity
@Table(name = "poi")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long position;

    @Nullable
    @Size(max = 50)
    private String nameLocationPoI;

    //@NotBlank -- We are not sure that we will receive an image from HAI Server
    @Nullable

    private String imagePoI;

    @Nullable
    private String subtypePoI;

    @Nullable
    private String placeOfBelongingPoI;

    @Nullable
    private String provincePoI;

    @Nullable
    private String abbreviationProvincePoI;

    @Nullable
    private String addressPoI;

    @Nullable
    private String metroPoI;

    @Nullable
    private String landlinePhoneNumberPoI;

    @Nullable
    private String cellPhoneNumberPoI;

    @Nullable
    private String otherPhoneNumberPoI;

    @Nullable
    @Email
    private String emailOfPoI;

    @Nullable
    private String websiteOfPoI;

    @Nullable
    @Column(columnDefinition = "TEXT")
    private String descriptionPoI;

    @Nullable
    private String ratingRossePoI;

    @Nullable
    private Double longitudeLocationPoI;

    @Nullable
    private Double latitudeLocationPoI;

    @Nullable
    private String hashtagsPoI;

    @Nullable
    private String audioPoI;

    @Nullable
    private Double ranking;

    @Nullable
    private Integer counter;

    @Nullable
    private Boolean isPublishedToFB;

    @Nullable
    private Boolean isPublishedToInsta;

    @Nullable
    private Boolean isPersonalPoI;

    @Nullable
    private Boolean isFavoritePoI;

}
