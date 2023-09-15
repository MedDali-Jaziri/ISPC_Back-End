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
    @Column(columnDefinition = "TEXT")
    private String descriptionLong;

    @NotBlank
    private String nameLocationTrip;

    @Nullable
    private String nameLocationTripUpdate;

    private Boolean isPublishedToHerMeS;

    private Boolean isPersonalTrip;

    private Boolean isFavoriteTrip;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "pois_trip",
            joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "poi_id"))
    private Set<PoI> poIS = new HashSet<>();
}
