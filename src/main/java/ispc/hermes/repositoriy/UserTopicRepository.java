package ispc.hermes.repositoriy;

import ispc.hermes.model.TopicHAI;
import ispc.hermes.model.User;
import ispc.hermes.model.UserTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserTopicRepository extends JpaRepository<UserTopic, Long> {

    Set<UserTopic> findAllByUser_Id(Long userId);

    Boolean existsByUserAndTopicHAI(User user, TopicHAI topicHAI);
}
