package ispc.hermes.repositoriy;

import ispc.hermes.model.ERole;
import ispc.hermes.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
