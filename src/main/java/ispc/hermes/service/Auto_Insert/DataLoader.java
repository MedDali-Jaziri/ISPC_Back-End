package ispc.hermes.service.Auto_Insert;

import ispc.hermes.model.ERole;
import ispc.hermes.model.Role;
import ispc.hermes.repositoriy.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Insert roles if not already present
        for (ERole roleName: ERole.values()){
            Role role = this.roleRepository.findRoleByName(roleName);
            if (role == null){
                role = new Role();
                role.setName(roleName);
                this.roleRepository.save(role);
            }
        }
    }
}
