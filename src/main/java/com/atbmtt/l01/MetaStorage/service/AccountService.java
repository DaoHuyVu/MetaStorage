package com.atbmtt.l01.MetaStorage.service;

import com.atbmtt.l01.MetaStorage.dao.ActivateToken;
import com.atbmtt.l01.MetaStorage.dao.User;
import com.atbmtt.l01.MetaStorage.dao.UserAccount;
import com.atbmtt.l01.MetaStorage.dto.ActivateTokenDto;
import com.atbmtt.l01.MetaStorage.dto.UserAccountDto;
import com.atbmtt.l01.MetaStorage.dto.UserDto;
import com.atbmtt.l01.MetaStorage.exception.TokenExpiredException;
import com.atbmtt.l01.MetaStorage.exception.UserExistException;
import com.atbmtt.l01.MetaStorage.repository.ActivateTokenRepository;
import com.atbmtt.l01.MetaStorage.repository.UserAccountRepository;
import com.atbmtt.l01.MetaStorage.repository.UserRepository;
import com.atbmtt.l01.MetaStorage.response.LoginResponse;
import com.atbmtt.l01.MetaStorage.security.JwtUtils;
import com.atbmtt.l01.MetaStorage.security.service.UserDetailsImpl;
import com.atbmtt.l01.MetaStorage.util.RandomCharacterGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional(readOnly = true)
@SuppressWarnings("unused")
public class AccountService {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAccountRepository accountRepository;
    @Autowired
    private ActivateTokenRepository activateTokenRepository;
    @Autowired
    private JavaMailSender sender;
    @Autowired
    private AuthenticationProvider provider;
    @Autowired
    private PasswordEncoder encoder;
    private final Logger logger = LoggerFactory.getLogger(AccountService.class);
    @Value("${spring.mail.username}")
    private String adminEmail;
    @Value("${SERVER_HOST}")
    private String serverHost;
    public LoginResponse login(String email, String password){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                email,password
        );
        Authentication authentication = provider.authenticate(token);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String accessToken = jwtUtils.createJwt(userDetails.getUsername());
        UserDto userDto = userRepository.findUserById(userDetails.getId());
        return new LoginResponse(
                accessToken,
                new UserAccountDto(
                        userDto.getName(),
                        userDetails.getUsername()
                )
        );
    }
    @Transactional
    public void signUp(
            String userName,
            String email,
            String password
    ){
        UserAccount a = accountRepository.findByEmail(email);
        if(a != null){
            throw new UserExistException("This email has already been used");
        }
        User user = new User(userName);
        UserAccount account = new UserAccount(email, encoder.encode(password));
        user.setUserAccount(account);
        String tokenString = RandomCharacterGenerator.getString((byte)64);
        ActivateToken token = new ActivateToken(tokenString);
        account.addToken(token);
        accountRepository.save(account);
        userRepository.save(user);
        activateTokenRepository.save(token);

        sendEmailVerification(userName,email, tokenString);
    }
    private void sendEmailVerification(String customerName,String email,String token){
        try{
            String fromAddress = adminEmail;
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.addTo(email);
            helper.setFrom(fromAddress);
            helper.setSubject("Activate account");
            String content = getString(customerName,token);
            helper.setText(content,true);
            sender.send(message);
        }catch(MessagingException ex){
            logger.error("Can't send verification Email");
        }
    }
    private String getString(String customerName,String token){
        String verifyUrl = serverHost + "/account/verify?confirm=" + token;
        return String.format(
                "Dear %s,<br>"
                        + "Please click the link below to verify your registration:<br>"
                        + "<h3><a rel =\"icon\" href=\"%s\" target=\"_self\">%s</a></h3>"
                        + "Thank you,<br>"
                        + "Meta.",customerName,verifyUrl,verifyUrl
        );
    }
    @Transactional
    public void verify(String token){
        ActivateTokenDto activateToken = activateTokenRepository.findByToken(token);
        System.out.println(token);
        if(activateToken.isExpired()){
            throw new TokenExpiredException("Your verification token has expired");
        }
        UserAccount account = accountRepository.findById(activateToken.getId()).orElseThrow();
        account.setIsActivated(true);
        accountRepository.save(account);
    }
}
