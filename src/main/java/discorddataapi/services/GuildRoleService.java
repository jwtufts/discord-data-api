package discorddataapi.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import discorddataapi.models.Role;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GuildRoleService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String guildId;
    private final String botToken;

    private Map<String, Role> roleCache = new HashMap<>();

    public GuildRoleService(
            @Value("${discord.guild-id}") String guildId,
            @Value("${discord.bot-token}") String botToken,
            ObjectMapper objectMapper) {
        this.webClient = WebClient.create("https://discord.com/api/v10");
        this.guildId = guildId;
        this.botToken = botToken;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        refreshRoles();
    }

    @Scheduled(fixedRate = 3600000) // every hour
    public void refreshRoles() {
        try {
            String response = webClient.get()
                    .uri("/guilds/{guildId}/roles", guildId)
                    .headers(h -> h.set("Authorization", "Bot " + botToken))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            List<Role> roles = objectMapper.readValue(response, new TypeReference<>() {});
            Map<String, Role> newCache = roles.stream()
                    .collect(Collectors.toMap(
                            r -> r.getName().toLowerCase(),
                            r -> r,
                            (a, b) -> a
                    ));

            this.roleCache = newCache;

            System.out.println("✅ Refreshed guild roles (" + newCache.size() + " roles)");
        } catch (Exception e) {
            System.err.println("❌ Failed to refresh roles: " + e.getMessage());
        }
    }

    public Optional<Role> getRoleByName(String roleName) {
        return Optional.ofNullable(roleCache.get(roleName.toLowerCase()));
    }

    public boolean userHasRole(List<String> userRoleIds, String roleName) {
        return getRoleByName(roleName)
                .map(role -> userRoleIds.contains(role.getId()))
                .orElse(false);
    }
}
