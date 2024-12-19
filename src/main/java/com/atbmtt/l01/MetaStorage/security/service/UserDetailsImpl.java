package com.atbmtt.l01.MetaStorage.security.service;

import com.atbmtt.l01.MetaStorage.ERole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
@Getter
public class UserDetailsImpl implements UserDetails {
    private final String userName;
    private final String password;
    private final Boolean isEnable;
    private final Long id;
    private final Set<GrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return userName;
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
        return isEnable;
    }
    UserDetailsImpl(Long id,String userName, String password, Boolean isEnable, Set<ERole> roles){
        this.userName = userName;
        this.password = password;
        this.isEnable = isEnable;
        this.id = id;
        authorities = roles
                .stream()
                .map(
                        role -> new SimpleGrantedAuthority(role.name())
                ).collect(Collectors.toUnmodifiableSet());
    }
}
