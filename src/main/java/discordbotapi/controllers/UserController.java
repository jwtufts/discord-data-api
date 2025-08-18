package discordbotapi.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/me")
    public String me(@AuthenticationPrincipal OAuth2User principal) {
        return "Logged in as: " + principal.getAttribute("username") +
                "#" + principal.getAttribute("discriminator");
    }
}
