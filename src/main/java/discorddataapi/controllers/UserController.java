package discorddataapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(Map.of(
                "id", Objects.requireNonNull(principal.getAttribute("id")),
                "username", Objects.requireNonNull(principal.getAttribute("username")),
                "discriminator", Objects.requireNonNull(principal.getAttribute("discriminator")),
                "email", Objects.requireNonNull(principal.getAttribute("email")),
                "avatar", Objects.requireNonNull(principal.getAttribute("avatar"))
        ));
    }
}
