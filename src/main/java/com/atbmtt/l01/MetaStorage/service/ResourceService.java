package com.atbmtt.l01.MetaStorage.service;

import com.atbmtt.l01.MetaStorage.dao.Resource;
import com.atbmtt.l01.MetaStorage.dao.UserAccount;
import com.atbmtt.l01.MetaStorage.dto.ResourceDto;
import com.atbmtt.l01.MetaStorage.exception.PasswordNotProvideException;
import com.atbmtt.l01.MetaStorage.repository.ResourceRepository;
import com.atbmtt.l01.MetaStorage.repository.UserAccountRepository;
import com.atbmtt.l01.MetaStorage.repository.UserResourceRepository;
import com.atbmtt.l01.MetaStorage.util.RandomCharacterGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ResourceService {
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private UserResourceRepository userResourceRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Transactional
    public ResourceDto postResource(
        MultipartFile file
    ){
        String fileUri = RandomCharacterGenerator.getString((byte)64);
        LocalDateTime now = LocalDateTime.now();
        Resource resource = new Resource(
                file.getOriginalFilename(),
                now,now,
                fileUri,
                file.getSize()
        );
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = (String) token.getPrincipal();
        UserAccount account = userAccountRepository.findByEmail(userName).orElseThrow();
        resource.addOwner(account);
        resourceRepository.save(resource);
        return new ResourceDto(
                resource.getId(),
                file.getOriginalFilename(),
                now,now,
                file.getSize(),
                fileUri,
                false,
                false,
                false,
                null
        );
    }
    @Transactional
    public List<ResourceDto> getOwnerResources(){
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = (String) token.getPrincipal();
        return resourceRepository.findByOwnerName(userName).orElse(new ArrayList<>());
    }
    @Transactional
    public ResourceDto changeResourceInfo(Map<String,String> fields,Long id){
        Resource resource = resourceRepository.findById(id).orElseThrow();
        fields.forEach((key,value) -> {
            if(key.equals("is_favourite")){
                resource.setIsFavourite(Boolean.parseBoolean(value));
                resource.setLastUpdate(LocalDateTime.now());
            }
            else if(key.equals("is_temp_delete")){
                resource.setIsTempDelete(Boolean.parseBoolean(value));
                resource.setLastUpdate(LocalDateTime.now());
            }
        });
        resourceRepository.save(resource);
        return new ResourceDto(
                resource
        );
    }
    public void inspectPermission(String uri){
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = (String) token.getPrincipal();
        UserAccount userAccount = userAccountRepository.findByEmail(userName).orElseThrow();
        String[] temp = uri.split("#");
        Resource resource = resourceRepository.findByUri(temp[0]).orElseThrow();
        if(!userResourceRepository.checkExist(userAccount.getId(),resource.getId())){
            if(temp.length == 1){
                throw new PasswordNotProvideException("Password for resource is not provided");
            }
            else if(temp.length == 2){
                if(encoder.matches(temp[1],resource.getPassword())){
                    resource.addReceiver(userAccount);
                }
                else
                    throw new BadCredentialsException("Password for resource is not correct");
            }
        }
    }
    @Transactional
    public ResourceDto changeResourcePassword(Long id, String password){
        Resource resource = resourceRepository.findById(id).orElseThrow();
        if(resource.getPassword() == null){
            resource.setSharedAt(LocalDateTime.now());
        }
        resource.setPassword(encoder.encode(password));
        resourceRepository.save(resource);
        return new ResourceDto(resource);
    }
    public ResourceDto getResourceDtoFromUri(String uri){
        Resource resource = resourceRepository.findByUri(uri).orElseThrow();
        return new ResourceDto(resource);
    }
}
