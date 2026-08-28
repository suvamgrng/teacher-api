package com.suvam.teacherapi.service;

import com.suvam.teacherapi.model.CustomUserDetails;
import com.suvam.teacherapi.model.Users;
import com.suvam.teacherapi.repository.UsersRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepo repo;

    public CustomUserDetailsService(UsersRepo repo) {
        this.repo = repo;
    }

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        Users user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new CustomUserDetails(user);
    }
}
