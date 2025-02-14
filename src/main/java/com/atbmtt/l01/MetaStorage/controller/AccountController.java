package com.atbmtt.l01.MetaStorage.controller;
import com.atbmtt.l01.MetaStorage.exception.TokenExpiredException;
import com.atbmtt.l01.MetaStorage.exception.UserExistException;
import com.atbmtt.l01.MetaStorage.request.LoginRequest;
import com.atbmtt.l01.MetaStorage.request.SignUpRequest;
import com.atbmtt.l01.MetaStorage.response.GenericResponse;
import com.atbmtt.l01.MetaStorage.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    private final ObjectMapper mapper = new ObjectMapper();
    @PostMapping("login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request
    ){
        try{
            return ResponseEntity.ok().body(accountService.login(request.getUserName(),request.getPassword()));
        }catch(BadCredentialsException exception){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,exception.getMessage(),exception);
        }catch(DisabledException exception){
            throw new ResponseStatusException(HttpStatus.CONFLICT,exception.getMessage(),exception);
        }
    }
    @PostMapping("signUp")
    public ResponseEntity<?> signUp(
            @RequestBody SignUpRequest request
    ){
        try{
            accountService.signUp(request.getUserName(),request.getEmail(),request.getPassword());
            return ResponseEntity.ok().body(
                    new GenericResponse(
                            "Account created successfully",
                            HttpStatus.CREATED.value(),
                            HttpStatus.CREATED.getReasonPhrase()
                    )
            );
        }catch (UserExistException ex){
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
    @PatchMapping("password")
    public ResponseEntity<?> updatePassword(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("email") String userName
    ){
        try{
            accountService.updatePassword(oldPassword,newPassword,userName);
            return ResponseEntity.ok().body("Change password successfully");
        }catch(Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getMessage(),
                    ex
            );
        }
    } @PatchMapping("email")
    public ResponseEntity<?> updateEmail(
            @RequestParam("newEmail") String newEmail,
            @RequestParam("email") String email
    ){
        try{
            accountService.updateEmail(newEmail,email);
            return ResponseEntity.ok().body("Change email successfully");
        }catch(Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getMessage(),
                    ex
            );
        }
    }
    @PostMapping("login/biometrics")
    public ResponseEntity<?> loginBiometrics(
            @RequestParam("email") String email,
            @RequestParam("name") String name
    ){
        try{
            return ResponseEntity.ok().body(accountService.loginBiometric(email,name));
        }catch(BadCredentialsException exception){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,exception.getMessage(),exception);
        }catch(DisabledException exception){
            throw new ResponseStatusException(HttpStatus.CONFLICT,exception.getMessage(),exception);
        }
    }
}
