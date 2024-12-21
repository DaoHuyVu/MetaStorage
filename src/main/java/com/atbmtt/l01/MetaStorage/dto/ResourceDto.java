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
@AllArgsConstructor
public class ResourceDto {
    private String name;
    private LocalDateTime uploadTime;
    private LocalDateTime lastUpdate;
    private Long capacity;
    private String uri;
    private String base64Content;

    public ResourceDto(String name, LocalDateTime uploadTime, LocalDateTime lastUpdate, Long capacity, String uri) {
        this.name = name;
        this.uploadTime = uploadTime;
        this.lastUpdate = lastUpdate;
        this.capacity = capacity;
        this.uri = uri;
    }
    public void setContent(InputStream inputStream) {
        try{
            this.base64Content = Base64.getEncoder().encodeToString(inputStream.readAllBytes());
        }catch(IOException e){
            throw new RuntimeException("Error encoding resource content",e);
        }
    }
}
