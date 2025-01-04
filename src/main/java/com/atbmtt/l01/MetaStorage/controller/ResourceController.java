package com.atbmtt.l01.MetaStorage.controller;

import com.atbmtt.l01.MetaStorage.dto.ResourceContent;
import com.atbmtt.l01.MetaStorage.dto.ResourceDto;
import com.atbmtt.l01.MetaStorage.service.ResourceService;
import com.atbmtt.l01.MetaStorage.service.S3Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@RestController
@RequestMapping("resource")
public class ResourceController {
    @Autowired
    private S3Service s3Service;
    @Autowired
    private ResourceService resourceService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @PostMapping("")
    public ResponseEntity<?> postResource(
            @RequestParam("fileContent") MultipartFile file
    ) {
       try{
           ResourceDto resourceDto = resourceService.postResource(file);
           s3Service.uploadResource(resourceDto.getUri(),file);
           return ResponseEntity.ok().body(
                   resourceDto
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
        return ResponseEntity.ok().body(resourceDtoList);
    }
    @GetMapping("{uri}")
    public ResponseEntity<?> getResourceContent(
            @PathVariable("uri") String uri
    ){
        ResourceContent resourceContent = new ResourceContent(uri);
        resourceContent.setContent(s3Service.getResource(uri));
        return ResponseEntity.ok().body(resourceContent);
    }
    @PatchMapping("")
    public ResponseEntity<?> changeResourceInfo(
            @RequestParam("fields") String f,
            @RequestParam("id") Long id
    )  {
        try{
            Map<String,String> fields = objectMapper.readValue(f, new TypeReference<>(){});
            return ResponseEntity.ok().body( resourceService.changeResourceInfo(fields,id));
        }
        catch(Exception exception){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    exception.getMessage(),
                    exception
            );
        }
    }
}
