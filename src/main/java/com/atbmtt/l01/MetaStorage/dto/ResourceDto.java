package com.atbmtt.l01.MetaStorage.dto;

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
    private String name;
    private LocalDateTime uploadTime;
    private LocalDateTime lastUpdate;
    private Long capacity;
    private String uri;
    private Boolean isFavourite;
    private Boolean isTempDelete;

    public ResourceDto(String name, LocalDateTime uploadTime, LocalDateTime lastUpdate, Long capacity, String uri,Boolean isFavourite,Boolean isTempDelete) {
        this.name = name;
        this.uploadTime = uploadTime;
        this.lastUpdate = lastUpdate;
        this.capacity = capacity;
        this.uri = uri;
        this.isFavourite = isFavourite;
        this.isTempDelete = isTempDelete;
    }

}
