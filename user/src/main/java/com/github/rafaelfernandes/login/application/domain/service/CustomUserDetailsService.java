package com.github.rafaelfernandes.login.application.domain.service;

import com.github.rafaelfernandes.login.application.port.out.LoginPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.github.rafaelfernandes.login.application.domain.model.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private LoginPort loginPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var login = loginPort.findByCellphone(username);

        return login.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
