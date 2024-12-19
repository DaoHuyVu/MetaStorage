package com.atbmtt.l01.MetaStorage.response;

import com.atbmtt.l01.MetaStorage.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private UserAccountDto userInfo;
}
