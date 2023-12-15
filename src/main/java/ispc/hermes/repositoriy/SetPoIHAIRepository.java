package ispc.hermes.repositoriy;

import ispc.hermes.model.SetPoIHAI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SetPoIHAIRepository extends JpaRepository<SetPoIHAI, Long> {
    Optional<SetPoIHAI> findSetPoIHAIById(Long id);
}
