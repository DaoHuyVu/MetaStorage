package com.atbmtt.l01.MetaStorage.service;

import com.atbmtt.l01.MetaStorage.dao.User;
import com.atbmtt.l01.MetaStorage.dto.UserAccountDto;
import com.atbmtt.l01.MetaStorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void updateUserName(String email,String newUserName){
        User user = userRepository.findUserByEmail(email);
        user.setUserName(newUserName);
    }
}
