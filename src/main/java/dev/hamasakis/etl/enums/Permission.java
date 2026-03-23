package dev.hamasakis.etl.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    // Permissões do domínio de ETL
    ETL_READ("etl:read"),
    ETL_WRITE("etl:write"),
    ETL_DELETE("etl:delete"),

    // Permissões de Administração de Usuários
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    USER_DELETE("user:delete");

    @Getter
    private final String permission;
}
