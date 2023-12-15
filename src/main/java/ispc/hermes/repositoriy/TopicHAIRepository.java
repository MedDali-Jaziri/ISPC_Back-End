package ispc.hermes.repositoriy;

import ispc.hermes.model.TopicHAI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicHAIRepository extends JpaRepository<TopicHAI, Long> {
    Boolean existsByLabel(String label);

    Optional<TopicHAI> findTopicHAIByLabel(String label);
}
