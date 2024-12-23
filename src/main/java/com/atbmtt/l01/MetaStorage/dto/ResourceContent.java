package com.atbmtt.l01.MetaStorage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Setter
@NoArgsConstructor
@Getter
public class ResourceContent {
    private String uri;
    private String base64Content;
    public ResourceContent(String uri){
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
