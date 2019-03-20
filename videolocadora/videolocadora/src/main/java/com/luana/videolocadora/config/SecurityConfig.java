package com.luana.videolocadora.config;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
 
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	

	@Autowired
    private CustomAuthenticationProvider authProvider;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
    	http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/users").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.PUT, "/users/*").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.GET, "/users").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/**").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.POST, "/movies").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.PUT, "/movies/*").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.GET, "/movies").hasAnyAuthority("ADMIN", "USER")
        .antMatchers(HttpMethod.POST, "/rents/*").hasAnyAuthority("ADMIN", "USER")
        .antMatchers(HttpMethod.PUT, "/rents/*").hasAnyAuthority("ADMIN", "USER")
        .antMatchers(HttpMethod.GET, "/rents/*").hasAuthority("ADMIN")
            .and().httpBasic()
            .and().csrf().disable();
 	
    	    	
        /*http.httpBasic()
        .and()
        .authorizeRequests()
        .anyRequest().authenticated().and().csrf().disable();*/
        

    }
  
}