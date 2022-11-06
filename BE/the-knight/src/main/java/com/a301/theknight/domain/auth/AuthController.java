package com.a301.theknight.domain.auth;

import com.a301.theknight.domain.auth.util.TokenProperties;
import com.a301.theknight.domain.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final TokenService tokenService;
    private final TokenProperties tokenProperties;

    @GetMapping("/websocket")
    public ResponseEntity<?> authWebsocket(@RequestParam String token) {
        if (!tokenService.validateToken(token, tokenProperties.getAccess().getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }
}
