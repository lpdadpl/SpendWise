package com.hazerta.SpendwiseBack.service;

import com.hazerta.SpendwiseBack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.hazerta.SpendwiseBack.entity.User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Usuario no encontrado con username/email: " + username));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                java.util.Collections
                        .singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().name())));
    }
}