package com.atbmtt.l01.MetaStorage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SharedResource {
    private String name;
    private Long capacity;
    private String uri;
}

