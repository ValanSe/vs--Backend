package com.valanse.valanse.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverUri;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoUri;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleUri;

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
                // Optionally, redirect to a default page or an error page
                break;
        }

        return new RedirectView(redirectUrl);
    }
}
