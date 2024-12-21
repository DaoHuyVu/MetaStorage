package com.atbmtt.l01.MetaStorage.service;

import com.atbmtt.l01.MetaStorage.dao.Resource;
import com.atbmtt.l01.MetaStorage.dao.UserAccount;
import com.atbmtt.l01.MetaStorage.dao.UserResource;
import com.atbmtt.l01.MetaStorage.dto.ResourceDto;
import com.atbmtt.l01.MetaStorage.repository.ResourceRepository;
import com.atbmtt.l01.MetaStorage.repository.UserAccountRepository;
import com.atbmtt.l01.MetaStorage.repository.UserResourceRepository;
import com.atbmtt.l01.MetaStorage.security.service.UserDetailsImpl;
import com.atbmtt.l01.MetaStorage.security.service.UserDetailsServiceImpl;
import com.atbmtt.l01.MetaStorage.util.RandomCharacterGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class ResourceService {
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private UserResourceRepository userResourceRepository;
    @Transactional
    public String postResource(
        MultipartFile file
    ){
        String fileUri = RandomCharacterGenerator.getString((byte)64) + "-" + file.getOriginalFilename();
        Resource resource = new Resource(
                file.getOriginalFilename(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                fileUri,
                file.getSize()
        );
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = (String) token.getPrincipal();
        UserAccount account = userAccountRepository.findByEmail(userName).orElseThrow();
        UserResource userResource = new UserResource(resource,account);
        userResource.setOwner(true);
        resource.addUser(account);
        resourceRepository.save(resource);
        return fileUri;
    }
    public List<ResourceDto> getOwnerResources(){
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = (String) token.getPrincipal();
        return resourceRepository.findByOwnerName(userName).orElse(new ArrayList<>());
    }

}
