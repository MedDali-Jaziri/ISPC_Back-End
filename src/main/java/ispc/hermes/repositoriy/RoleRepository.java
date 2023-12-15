package ispc.hermes.repositoriy;

import ispc.hermes.model.ERole;
import ispc.hermes.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);

    Role findRoleByName(ERole name);
}
