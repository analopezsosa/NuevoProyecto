package com.example.NuevoProyecto.Security;

import com.example.NuevoProyecto.Security.RepositoryUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    RepositoryUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(15, new SecureRandom());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http.authorizeRequests().antMatchers("/users/login").permitAll();
        http.authorizeRequests().antMatchers("/users/signup").permitAll();
       // http.authorizeRequests().antMatchers("/users/{name}").hasAnyRole("USER","ADMIN");
       // http.authorizeRequests().antMatchers("/users/{name}/editPass").hasAnyRole("USER","ADMIN");
        //http.authorizeRequests().antMatchers("/users/{name}/editDesc").hasAnyRole("USER","ADMIN");
        //http.authorizeRequests().antMatchers("/users/{name}/delete").hasAnyRole("USER","ADMIN");

        http.authorizeRequests().antMatchers("/grades").permitAll();
       // http.authorizeRequests().antMatchers("/grades/create").hasRole("USER");
        http.authorizeRequests().antMatchers("/grades/join").hasRole("USER");
      //http.authorizeRequests().antMatchers("/grades/{id}").hasAnyRole("USER");
        http.authorizeRequests().antMatchers("/grades/{id}/delete").hasAnyRole("USER","ADMIN");



        http.authorizeRequests().antMatchers("/subjects").permitAll();
      //  http.authorizeRequests().antMatchers("/subjects/create").hasRole("USER");
        http.authorizeRequests().antMatchers("/subjects/join").hasRole("USER");
        //http.authorizeRequests().antMatchers("/grades/{id}").hasAnyRole("USER");
        http.authorizeRequests().antMatchers("/subjects/{id}/delete").hasAnyRole("USER","ADMIN");

        http.authorizeRequests().antMatchers("/admin").hasRole("ADMIN");
        http.authorizeRequests().antMatchers("/").permitAll();

        http.formLogin().loginPage("/login");
        http.formLogin().usernameParameter("username");
        http.formLogin().passwordParameter("password");
        http.formLogin().failureUrl("/error");

        http.logout().logoutSuccessUrl("/");
    }
}