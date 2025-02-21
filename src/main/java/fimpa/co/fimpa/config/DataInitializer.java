package fimpa.co.fimpa.config;

import fimpa.co.fimpa.model.Role;
import fimpa.co.fimpa.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class DataInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    @SuppressWarnings("unused")
    CommandLineRunner initRoles() {
        return args -> {
            createRoleIfNotFound("USER");
            createRoleIfNotFound("ADMIN");
        };
    }

    private void createRoleIfNotFound(String roleName) {
        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        if (!roleOpt.isPresent()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
