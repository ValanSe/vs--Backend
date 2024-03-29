package com.valanse.valanse.dto.response;

import com.valanse.valanse.dto.StatusResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenResponseStatus {

    private Integer status;
    private String accessToken;

    public static TokenResponseStatus addStatus(Integer status, String accessToken) {
        return new TokenResponseStatus(status, accessToken);
    }

    public static TokenResponseStatus success(String accessToken){
        return new TokenResponseStatus(200, accessToken);
    }
}




