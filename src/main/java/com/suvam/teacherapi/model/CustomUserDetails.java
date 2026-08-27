package com.suvam.teacherapi.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class CustomUserDetails implements UserDetails {

    private final Users users;

    public CustomUserDetails(Users users) {
        this.users = users;
    }

    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     *
     * @return the authorities, sorted by natural key (never <code>null</code>)
     */
    @Override
    public @NullMarked Collection<? extends GrantedAuthority> getAuthorities() {

        if (users.getRole() != null && !users.getRole().isBlank()) {
            return Stream.of(users.getRole().split(","))
                    .map(String::trim)
                    .map(role -> role.startsWith("ROLE_") ? role: "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        }
        return List.of();
    }

    /**
     * Returns the password used to authenticate the user. Can be null if the user has not
     * specified a password (e.g. the user Passkeys instead).
     *
     * @return the password
     */
    @Override
    public @Nullable String getPassword() {
        return users.getPassword();
    }

    /**
     * Returns the username used to authenticate the user. Cannot return
     * <code>null</code>.
     *
     * @return the username (never <code>null</code>)
     */
    @Override
    public String getUsername() {
        return users.getUsername();
    }
}
