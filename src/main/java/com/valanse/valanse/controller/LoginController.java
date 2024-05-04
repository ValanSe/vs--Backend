package com.valanse.valanse.controller;

import com.valanse.valanse.dto.StatusResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "oauth login", description = "oauth login 페이지로 이동합니다.")
public class LoginController {

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverUri;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoUri;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleUri;



    @Operation(summary = "OAuth 제공자에 따라 로그인을 수행합니다.",
            description = "지정된 OAuth 제공자에 따라 해당하는 인증 페이지로 리디렉션합니다.")
    @Parameter(name = "oauthProvider",
            description = "OAuth 제공자 선택",
            example = "naver",
            required = true,
            schema = @Schema(allowableValues = {"naver", "kakao", "google"}))
    @ApiResponses(
            value = {
            @ApiResponse(responseCode = "200", description = "리디렉션 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
            @ApiResponse(responseCode = "404", description = "지정된 OAuth 제공자를 찾을 수 없음")
    })
    @PostMapping("/oauth/{oauthProvider}")
    public RedirectView login(@PathVariable("oauthProvider") String oauthProvider) {
        String redirectUrl = "";

        switch (oauthProvider.toLowerCase()) {
            case "naver":
                redirectUrl = naverUri;
                break;
            case "kakao":
                redirectUrl = kakaoUri;
                break;
            case "google":
                redirectUrl = googleUri;
                break;
            default:
                log.error("Unsupported OAuth provider: {}", oauthProvider);
                break;
        }

        return new RedirectView(redirectUrl);
    }
}
