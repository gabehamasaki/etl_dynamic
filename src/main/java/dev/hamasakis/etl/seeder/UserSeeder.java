package dev.hamasakis.etl.seeder;

import dev.hamasakis.etl.enums.Role;
import dev.hamasakis.etl.models.User;
import dev.hamasakis.etl.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("gabriel@hamasakis.dev").isEmpty()) {
            User user = User.builder()
                    .name("Gabriel Hamasaki")
                    .email("gabriel@hamasakis.dev")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(user);

            System.out.println("User seeded: " + user.getEmail());
            return;
        }

        System.out.println("✅ SEEDER: All users already exist");
    }
}
