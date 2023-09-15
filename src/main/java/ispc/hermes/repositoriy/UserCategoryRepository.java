package ispc.hermes.repositoriy;

import ispc.hermes.model.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCategoryRepository extends JpaRepository<UserCategory, Integer> {

    Optional<UserCategory> findByUserId(Long id);


    Optional<UserCategory> findByUserIdAndPoIPosition(Long id, Long position);
}
