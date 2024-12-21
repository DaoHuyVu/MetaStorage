package com.atbmtt.l01.MetaStorage.security.service;

import com.atbmtt.l01.MetaStorage.dao.UserAccount;
import com.atbmtt.l01.MetaStorage.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByEmail(username).orElseThrow();
        return new UserDetailsImpl(
                userAccount.getId(),
                userAccount.getEmail(),
                userAccount.getPassword(),
                userAccount.getIsActivated(),
                Set.of(userAccount.getRole())
        );
    }
}
