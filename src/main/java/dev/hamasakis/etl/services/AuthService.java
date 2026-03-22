package dev.hamasakis.etl.services;

import dev.hamasakis.etl.repositories.UserReporsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserReporsitory userReporsitory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userReporsitory.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
