package com.atbmtt.l01.MetaStorage.controller;
import com.atbmtt.l01.MetaStorage.exception.TokenExpiredException;
import com.atbmtt.l01.MetaStorage.exception.UserExistException;
import com.atbmtt.l01.MetaStorage.response.GenericResponse;
import com.atbmtt.l01.MetaStorage.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @PostMapping("login")
    public ResponseEntity<?> login(
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ){
        return ResponseEntity.ok().body(accountService.login(email,password));
    }
    @PostMapping("signUp")
    public ResponseEntity<?> signUp(
            @RequestParam("userName") String userName,
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ){
        try{
            accountService.signUp(userName,email,password);
            return ResponseEntity.ok().body(
                    new GenericResponse(
                            "Account created successfully",
                            HttpStatus.CREATED.value(),
                            HttpStatus.CREATED.getReasonPhrase()
                    )
            );
        }catch (NoSuchElementException ex){
            throw new ResponseStatusException(HttpStatus.CONFLICT,ex.getMessage(),ex);
        }
    }
    @GetMapping("verify")
    public ResponseEntity<?> verify(
            @RequestParam("confirm") String token
    ){
        try{
            accountService.verify(token);
            return ResponseEntity.ok().body(
                    new GenericResponse(
                            "Verify email successfully",
                            HttpStatus.OK.value(),
                            HttpStatus.OK.getReasonPhrase()
                    )
            );
        }catch(TokenExpiredException exception){
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getLocalizedMessage(),exception);
        }
    }
    @PostMapping("token")
    public ResponseEntity<?> getToken(
            @RequestParam("userName") String userName,
            @RequestParam("email") String email
    ){
        try{
            accountService.getToken(userName,email);
            return ResponseEntity.ok().body(
                    new GenericResponse(
                            "Create token successfully",
                            HttpStatus.CREATED.value(),
                            HttpStatus.CREATED.getReasonPhrase()
                    )
            );
        }catch(NoSuchElementException ex){
            throw new ResponseStatusException(HttpStatus.CONFLICT,ex.getMessage(),ex);
        }
    }
}
