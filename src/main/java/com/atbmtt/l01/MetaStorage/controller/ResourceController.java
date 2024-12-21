package com.atbmtt.l01.MetaStorage.controller;

import com.atbmtt.l01.MetaStorage.dto.ResourceDto;
import com.atbmtt.l01.MetaStorage.response.GenericResponse;
import com.atbmtt.l01.MetaStorage.service.ResourceService;
import com.atbmtt.l01.MetaStorage.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("resource")
public class ResourceController {
    @Autowired
    private S3Service s3Service;
    @Autowired
    private ResourceService resourceService;
    @PostMapping("")
    public ResponseEntity<?> postResource(
            @RequestParam("fileContent") MultipartFile file
    ) {
       try{
           String fileUri = resourceService.postResource(file);
           s3Service.uploadResource(fileUri,file);
           return ResponseEntity.ok().body(
                   new GenericResponse(
                           "Upload resource successfully",
                           HttpStatus.OK.value(),
                           HttpStatus.OK.getReasonPhrase()
                   )
           );
       }catch(NoSuchElementException exception){
           throw new ResponseStatusException(
                   HttpStatus.NOT_FOUND,
                   exception.getMessage(),
                   exception
           );
       } catch(IOException exception){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    exception.getMessage(),
                    exception
            );
       }
    }
    @GetMapping("")
    public ResponseEntity<?> getOwnerResources(){
        List<ResourceDto> resourceDtoList = resourceService.getOwnerResources();
        resourceDtoList.forEach(resource ->
                    resource.setContent(s3Service.getResource(resource.getUri()))
                );
        return ResponseEntity.ok().body(resourceDtoList);
    }
}
