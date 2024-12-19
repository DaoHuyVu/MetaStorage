package com.atbmtt.l01.MetaStorage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivateTokenDto {
    private String token;
    private Date expiredTime;
    private Long id;
    public Boolean isExpired(){
        return expiredTime.before(new Date(System.currentTimeMillis()));
    }
}
