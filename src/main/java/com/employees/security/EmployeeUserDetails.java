package com.employees.security;

import com.employees.models.AccountInformation;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EmployeeUserDetails implements UserDetails {

    private AccountInformation accountInformation;

    public EmployeeUserDetails(AccountInformation accountInformation){
        this.accountInformation = accountInformation;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> role = new ArrayList<GrantedAuthority>();
        GrantedAuthority auth = new SimpleGrantedAuthority("ROLE_" + accountInformation.getRole());
        role.add(auth);
        return role;
    }

    @Override
    public String getPassword() {
        return accountInformation.getPassword();
    }

    @Override
    public String getUsername() {
        return accountInformation.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
