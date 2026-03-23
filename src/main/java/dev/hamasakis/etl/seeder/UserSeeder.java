package dev.hamasakis.etl.seeder;

import dev.hamasakis.etl.models.User;
import dev.hamasakis.etl.repositories.UserReporsitory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserSeeder implements CommandLineRunner {

    private final UserReporsitory userReporsitory;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserReporsitory userReporsitory, PasswordEncoder passwordEncoder) {
        this.userReporsitory = userReporsitory;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userReporsitory.findByEmail("gabriel@hamasakis.dev").isEmpty()) {
            User user = User.builder()
                    .email("gabriel@hamasakis.dev")
                    .password(passwordEncoder.encode("password"))
                    .build();

            userReporsitory.save(user);

            System.out.println("User seeded: " + user.getEmail());
            return;
        }

        System.out.println("✅ SEEDER: All users already exist");
    }
}
