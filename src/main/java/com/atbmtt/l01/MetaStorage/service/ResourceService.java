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
import java.util.Map;

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
                resource.getId(),
                file.getOriginalFilename(),
                now,now,
                file.getSize(),
                fileUri,
                false,
                false
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
}
