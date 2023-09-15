package ispc.hermes.repositoriy;

import ispc.hermes.model.PoI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PoIRepository extends JpaRepository<PoI, Long> {
    Optional<PoI> findByPosition(Long position);

    Set<PoI> findAllByIsPersonalPoI(Boolean isPersonalPoI);
}
