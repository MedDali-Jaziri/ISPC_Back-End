package ispc.hermes.repositoriy;

import ispc.hermes.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
    Optional<Interest> findByNameInterstAndActivationInterst(String nameInterest, Boolean activationInterest);
}
