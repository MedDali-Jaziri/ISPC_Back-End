package ispc.hermes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ispc.hermes.model.Converter.BooleanListConverter;
import ispc.hermes.model.Converter.DoubleListConverter;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "set_poi_hai")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetPoIHAI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    private Integer duration;

    @Convert(converter = DoubleListConverter.class)
    @Size(min = 2, max = 3)
    private List<Double> userLocation;

    @Nullable
    private Integer groupSize;

    @Convert(converter = BooleanListConverter.class)
    @Size(min = 4, max = 4)
    private List<Boolean> dVector;

    @Convert(converter = BooleanListConverter.class)
    @Size(min = 4, max = 4)
    private List<Boolean> mVector;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;
}
