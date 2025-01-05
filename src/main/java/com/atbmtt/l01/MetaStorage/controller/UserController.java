package com.atbmtt.l01.MetaStorage.controller;

import com.atbmtt.l01.MetaStorage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    @PatchMapping("")
    public ResponseEntity<?> updateUserName(
            @RequestParam("email") String email,
            @RequestParam("userName") String userName
    ){
        try{
           userService.updateUserName(email,userName);
           return ResponseEntity.ok().body("Update userName successfully");
        }catch(Exception exception){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    exception.getMessage(),
                    exception
            );
        }
    }
}
