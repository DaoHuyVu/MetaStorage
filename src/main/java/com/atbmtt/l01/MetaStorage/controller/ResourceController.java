package com.atbmtt.l01.MetaStorage.controller;

import com.atbmtt.l01.MetaStorage.dto.ResourceDto;
import com.atbmtt.l01.MetaStorage.dto.SharedResource;
import com.atbmtt.l01.MetaStorage.exception.PasswordNotProvideException;
import com.atbmtt.l01.MetaStorage.service.ResourceService;
import com.atbmtt.l01.MetaStorage.service.S3Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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
        try{
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + uri + "\"")
                    .body(new InputStreamResource(s3Service.getResource(uri)));
        } catch(Exception exception){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    exception.getMessage(),
                    exception
            );
        }
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
    @PatchMapping("password")
    public ResponseEntity<?> changeResourcePassword(
            @RequestParam("password") String password,
            @RequestParam("id") Long id
    ){
        try{
            return ResponseEntity.ok().body(resourceService.changeResourcePassword(id,password));
        }catch(Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getMessage(),
                    ex
            );
        }
    }
    @PatchMapping("link/delete")
    public ResponseEntity<?> deleteLink(
            @RequestParam("uri") String uri
    ){
        try{
            return ResponseEntity.ok().body(resourceService.deleteLink(uri));
        }catch(Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getMessage(),
                    ex
            );
        }
    }
    @GetMapping("share/{uri}")
    public ResponseEntity<?> getSharedResource(
            @PathVariable("uri") String uri
    ) {
        try {
            resourceService.inspectPermission(uri);
            String[] temp = uri.split("#");
            ResourceDto resourceDto = resourceService.getResourceDtoFromUri(temp[0]);
            return ResponseEntity
                    .ok()
                    .body(
                            new SharedResource(
                                    resourceDto.getName(),
                                    resourceDto.getCapacity(),
                                    uri
                            )
                    );
        } catch (BadCredentialsException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    exception.getMessage(),
                    exception
            );
        } catch (PasswordNotProvideException exception) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    exception.getMessage(),
                    exception
            );
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    exception.getMessage(),
                    exception
            );
        }
    }
    @GetMapping("share/{uri}/content")
    public ResponseEntity<?> getSharedResourceContent(
            @PathVariable("uri") String uri
    ){
        try{
            resourceService.inspectPermission(uri);
            String[] temp = uri.split("#");
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + uri + "\"")
                    .body(
                            new InputStreamResource(s3Service.getResource(temp[0]))
                    );
        }catch(BadCredentialsException exception){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    exception.getMessage(),
                    exception
            );
        }
        catch (PasswordNotProvideException exception){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    exception.getMessage(),
                    exception
            );
        }
        catch(Exception exception){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    exception.getMessage(),
                    exception
            );
        }
    }
    @GetMapping("share")
    public ResponseEntity<?> getSharedResources(){
        try{

            return ResponseEntity.ok(resourceService.getSharedResources());
        }catch(Exception exception){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    exception.getMessage(),
                    exception
            );
        }
    }

}
