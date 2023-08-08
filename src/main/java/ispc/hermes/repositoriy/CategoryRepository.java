package ispc.hermes.repositoriy;

import ispc.hermes.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameCategoryAndActivationCategory(String nameCategory, Boolean activationCategory);
}
