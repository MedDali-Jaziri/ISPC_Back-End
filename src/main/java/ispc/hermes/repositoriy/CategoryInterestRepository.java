package ispc.hermes.repositoriy;

import ispc.hermes.model.CategoryInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryInterestRepository extends JpaRepository<CategoryInterest, Integer> {

    Optional<CategoryInterest> findByUserId(Long id);


    Optional<CategoryInterest> findByUserIdAndPoIPosition(Long id, Long position);
}
