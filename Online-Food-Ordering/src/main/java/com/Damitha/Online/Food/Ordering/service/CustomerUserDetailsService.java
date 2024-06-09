package com.Damitha.Online.Food.Ordering.service;

import com.Damitha.Online.Food.Ordering.model.User;
import com.Damitha.Online.Food.Ordering.model.UserRole;
import com.Damitha.Online.Food.Ordering.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service // Marks this class as a Spring Service component
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired // Automatically injects the UserRepository dependency
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Fetch the User object from the database using the provided email (username)
        User user = userRepository.findByEmail(username);

        // If user is not found, throw an exception
        if(user == null) {
            throw new UsernameNotFoundException("user not found with email " + username);
        }

        // Get the role of the user
        UserRole role = user.getRole();

        // Create a list to hold the user's authorities (roles)
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Convert the user's role to a GrantedAuthority and add it to the list
        authorities.add(new SimpleGrantedAuthority(role.toString()));

        // Create and return a Spring Security User object with the user's details and authorities
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
