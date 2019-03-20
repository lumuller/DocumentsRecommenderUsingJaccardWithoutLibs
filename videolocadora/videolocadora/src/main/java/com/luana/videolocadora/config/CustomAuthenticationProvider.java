package com.luana.videolocadora.config;

import java.util.ArrayList;
import java.util.List;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.luana.videolocadora.models.User;
import com.luana.videolocadora.repository.UsersRepository;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UsersRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication)  {
    	
        String email = authentication.getName();        
        String password = authentication.getCredentials().toString();       
        User usuario = userRepository.findByUsername(email);        

        if(usuario != null && usuario.getPassword().equals(password)) {
        	List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(usuario.getRole()));

            return new UsernamePasswordAuthenticationToken(email, password, authorities);
        } return null;       
        
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}