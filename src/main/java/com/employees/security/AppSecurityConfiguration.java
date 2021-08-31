//package com.employees.security;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;
//
//@Configuration
//@EnableWebSecurity
//public class AppSecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//         auth
//                 .inMemoryAuthentication()
//                .withUser(User.withDefaultPasswordEncoder().username("user").password("1234")
//                        .roles("USER"));
//    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/hr/**").hasRole("ADMIN")
//                .antMatchers("/employee/**").authenticated()
//                .and()
//                .httpBasic();
//    }
//
//
//}
