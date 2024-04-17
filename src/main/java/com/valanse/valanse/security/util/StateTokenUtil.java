package com.valanse.valanse.security.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StateTokenUtil {

    public String getStateToken() {
        String stateToken = UUID.randomUUID().toString();
        log.info("stateToken: {}", stateToken);
        return stateToken;
    }

}
