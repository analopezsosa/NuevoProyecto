package com.example.NuevoProyecto.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
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
        //http.antMatcher("/api/**");
        // Private endpoints
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/user/").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/user/{username}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"/api/user/{username}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE,"/api/user/{username}").hasRole("ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/subjects/").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/subjects/{id}").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/subjects/").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"/api/subjects/{id}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE,"/api/subjects/{id}").hasRole("ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/grades/").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/grades/{id}").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/grades/").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"/api/grades/{id}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE,"/api/grades/{id}").hasRole("ADMIN");


        http.csrf().disable();
        http.httpBasic();
        http.formLogin().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);











    }
}
