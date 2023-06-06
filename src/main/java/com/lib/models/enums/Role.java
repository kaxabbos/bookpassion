package com.lib.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN,
    PUB;

    @Override
    public String getAuthority() {
        return name();
    }
}
