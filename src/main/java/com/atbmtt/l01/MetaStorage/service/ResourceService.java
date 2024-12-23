package com.atbmtt.l01.MetaStorage.service;

import com.atbmtt.l01.MetaStorage.dao.Resource;
import com.atbmtt.l01.MetaStorage.dao.UserAccount;
import com.atbmtt.l01.MetaStorage.dto.ResourceDto;
import com.atbmtt.l01.MetaStorage.repository.ResourceRepository;
import com.atbmtt.l01.MetaStorage.repository.UserAccountRepository;
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

@Service
@Transactional(readOnly = true)
public class ResourceService {
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Transactional
    public ResourceDto postResource(
        MultipartFile file
    ){
        String fileUri = RandomCharacterGenerator.getString((byte)64) + "-" + file.getOriginalFilename();
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
                file.getOriginalFilename(),
                now,now,
                file.getSize(),
                fileUri,
                false,
                false
        );
    }
    public List<ResourceDto> getOwnerResources(){
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = (String) token.getPrincipal();
        return resourceRepository.findByOwnerName(userName).orElse(new ArrayList<>());
    }

}
