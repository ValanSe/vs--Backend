package com.valanse.valanse.security.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GeneratedToken {

    private final String accessToken;
    private final String refreshToken;


}
