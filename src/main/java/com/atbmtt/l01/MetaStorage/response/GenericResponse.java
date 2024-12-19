package com.atbmtt.l01.MetaStorage.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GenericResponse {
    private String message;
    private int code;
    private String messageCode;
}
