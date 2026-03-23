package dev.hamasakis.etl.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.hamasakis.etl.enums.Permission.*;

@RequiredArgsConstructor
public enum Role {
    // O USER só pode ler dados do ETL
    USER(Set.of(ETL_READ)),

    // O MANAGER pode ler e executar/escrever no ETL
    MANAGER(Set.of(ETL_READ, ETL_WRITE)),

    // O ADMIN pode fazer tudo
    ADMIN(Set.of(ETL_READ, ETL_WRITE, ETL_DELETE, USER_READ, USER_WRITE));

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}
