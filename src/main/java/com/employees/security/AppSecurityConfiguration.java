package com.employees.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class AppSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private EmployeeUserDetailsService employeeUserDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
       // PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().disable().csrf().disable()
                .authorizeRequests()
                .antMatchers("/employee/**").hasAnyRole("ADMIN","EMPLOYEE")
                .antMatchers("/hr/**").hasRole("ADMIN")
                .antMatchers("/team").hasRole("ADMIN")
                .antMatchers("/department").hasRole("ADMIN")
                .antMatchers("/").permitAll()
                .and()
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.employeeUserDetailsService);
        return daoAuthenticationProvider;
    }


}
