package ispc.hermes.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trip")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    private String descriptionBrief;

    @Nullable
    private String descriptionLong;

    @NotBlank
    private String nameLocationTrip;

    @NotBlank
    private String nameLocationTripUpdate;

    @Nullable
    private Boolean isPublishedToHerMeS;

    @Nullable
    private Boolean isPersonalTrip;

    @Nullable
    private Boolean isFavoriteTrip;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "pois_trip",
            joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "poi_id"))
    private Set<PoI> poIS = new HashSet<>();
}
