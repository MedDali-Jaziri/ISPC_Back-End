package ispc.hermes.repositoriy;

import ispc.hermes.model.PoI;
import ispc.hermes.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findTripByNameLocationTrip(String nameLocationTrip);

    Optional<Trip> findTripByNameLocationTripOrId(String nameLocationTrip, Long id);

    Optional<Trip> findTripByNameLocationTripAndIsFavoriteTrip(String nameLocationTrip, Boolean isFavoriteTrip);
}
