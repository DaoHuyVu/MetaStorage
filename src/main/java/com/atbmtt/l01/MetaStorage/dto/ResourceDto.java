package com.atbmtt.l01.MetaStorage.dto;

import com.atbmtt.l01.MetaStorage.dao.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
public class ResourceDto {
    private Long id;
    private String name;
    private LocalDateTime uploadTime;
    private LocalDateTime lastUpdate;
    private Long capacity;
    private String uri;
    private Boolean isFavourite;
    private Boolean isTempDelete;

    public ResourceDto(Long id,String name, LocalDateTime uploadTime, LocalDateTime lastUpdate, Long capacity, String uri,Boolean isFavourite,Boolean isTempDelete) {
        this.name = name;
        this.id = id;
        this.uploadTime = uploadTime;
        this.lastUpdate = lastUpdate;
        this.capacity = capacity;
        this.uri = uri;
        this.isFavourite = isFavourite;
        this.isTempDelete = isTempDelete;
    }
    public ResourceDto(Resource resource){
        this.id = resource.getId();
        this.name = resource.getName();
        this.uploadTime = resource.getUploadTime();
        this.lastUpdate = resource.getLastUpdate();
        this.capacity = resource.getCapacity();
        this.uri = resource.getUri();
        this.isFavourite = resource.getIsFavourite();
        this.isTempDelete = resource.getIsTempDelete();
    }
}
