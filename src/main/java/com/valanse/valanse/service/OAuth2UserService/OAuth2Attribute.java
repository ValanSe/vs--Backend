package com.valanse.valanse.service.OAuth2UserService;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@ToString
@Builder
@Getter
public class OAuth2Attribute {
    private Map<String, Object> attributes; // 사용자 속성 정보를 담는 Map
    private String attributeKey; // 사용자 속성의 키 값
    private String email; // 이메일 정보
    private String name; // 이름 정보
    private String picture; // 프로필 사진 정보
    private String provider; // 제공자 정보


}