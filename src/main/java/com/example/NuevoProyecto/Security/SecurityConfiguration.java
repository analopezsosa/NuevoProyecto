package com.example.NuevoProyecto.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;




@Configuration
@EnableWebSecurity

public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    RepositoryUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
// Public pages
    http.authorizeRequests().antMatchers("/").permitAll();
    http.authorizeRequests().antMatchers("/login").permitAll();
     http.authorizeRequests().antMatchers("/error").permitAll();
    http.authorizeRequests().antMatchers("/logout").permitAll();
     http.authorizeRequests().antMatchers("/signup").permitAll();



// Private pages (all other pages)
        http.authorizeRequests().anyRequest().authenticated();*/
// Login form
        http.formLogin().loginPage("/login");
        http.formLogin().usernameParameter("username");
        http.formLogin().passwordParameter("password");
        http.formLogin().failureUrl("/error");
// Logout
        http.logout().logoutUrl("/logout");
        http.logout().logoutSuccessUrl("/");
// Disable CSRF at the moment
        http.csrf().disable();


}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(15, new SecureRandom());
    }

}
