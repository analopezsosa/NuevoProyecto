package com.example.NuevoProyecto.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Order(1)
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    RepositoryUserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http)throws Exception{
        /*todavia no esta
        http.antMatcher("/api/**");
        // Private endpoints
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/").hasRole("USER");
        http.authorizeRequests().antMatchers("/api/user/").permitAll();
        http.authorizeRequests().antMatchers("/users/signup").permitAll();
        http.authorizeRequests().antMatchers("/users/joingradeU").hasRole("USER");
        http.authorizeRequests().antMatchers("/users/joingrade").hasRole("ADMIN");
        http.authorizeRequests().antMatchers("/users/filter").hasAnyRole("ADMIN");
        http.authorizeRequests().antMatchers("/users/{user}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers("/users/remove").hasRole("ADMIN");
        http.authorizeRequests().antMatchers("/users/removefromgrade").hasRole("ADMIN");
        */
    }
}
