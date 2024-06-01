package com.example.betme.security.provider;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class PlayerAuthProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }
        boolean isValidPasswordMatch = passwordEncoder.matches(password, userDetails.getPassword());
        if (isValidPasswordMatch) {
            Authentication authenticationResult =
                    new UsernamePasswordAuthenticationToken(username, password,
                            userDetails.getAuthorities());
            return authenticationResult;
        }

        throw new BadCredentialsException("incorrect credentials, username or password incorrect");

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
