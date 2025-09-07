package discorddataapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import discorddataapi.services.GuildRoleService;
import discorddataapi.services.MemberCacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/me")
public class UserController {
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final GuildRoleService guildRoleService;
    private final ObjectMapper objectMapper;
    private final MemberCacheService memberCacheService;
    @Value("${discord.guild-id}")
    private String guildId;

    public UserController(OAuth2AuthorizedClientService authorizedClientService, GuildRoleService guildRoleService, ObjectMapper objectMapper, MemberCacheService memberCacheService) {
        this.authorizedClientService = authorizedClientService;
        this.guildRoleService = guildRoleService;
        this.objectMapper = objectMapper;
        this.memberCacheService = memberCacheService;
    }

    @GetMapping
    public ResponseEntity<?> me(@AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(Map.of(
                "id", Objects.requireNonNull(principal.getAttribute("id")),
                "username", Objects.requireNonNull(principal.getAttribute("username")),
                "discriminator", Objects.requireNonNull(principal.getAttribute("discriminator")),
                "email", Objects.requireNonNull(principal.getAttribute("email")),
                "avatar", Objects.requireNonNull(principal.getAttribute("avatar"))
        ));
    }

    @GetMapping("/member")
    public ResponseEntity<String> userInfo(@AuthenticationPrincipal OAuth2User principal, OAuth2AuthenticationToken authentication) {
        String userId = principal.getAttribute("id");

        String cached = memberCacheService.get(userId);
        if (cached != null) {
            return ResponseEntity.ok(cached);
        }

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        String memberJson = WebClient.create("https://discord.com/api/v10")
                .get()
                .uri("/users/@me/guilds/{guildId}/member", guildId)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (memberJson != null) {
            memberCacheService.put(userId, memberJson);
        }

        return ResponseEntity.ok(memberJson);
    }

    @GetMapping("/has-role")
    public ResponseEntity<?> hasRole(
            @AuthenticationPrincipal OAuth2User principal,
            OAuth2AuthenticationToken authentication,
            @RequestParam String roleName) throws IOException {

        String memberJson = userInfo(principal, authentication).getBody();

        List<String> userRoleIds = objectMapper.readTree(memberJson)
                .get("roles")
                .traverse(objectMapper)
                .readValueAs(new TypeReference<List<String>>() {});

        boolean hasRole = guildRoleService.userHasRole(userRoleIds, roleName);

        return ResponseEntity.ok(Map.of("hasRole", hasRole));
    }
}
