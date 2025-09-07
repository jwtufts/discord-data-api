package discorddataapi.configuration;

import discorddataapi.services.MemberCacheService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
public class SecurityConfig {
    private final MemberCacheService memberCacheService;

    public SecurityConfig(MemberCacheService memberCacheService) {
        this.memberCacheService = memberCacheService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/discord/login").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("http://localhost:5173", true)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .cors(cors -> {})
                .sessionManagement(session -> session
                        .maximumSessions(1)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                    .logoutUrl("/logout")
                    .addLogoutHandler((request, response, authentication) -> {
                        if (authentication != null) {
                            String userId = ((OAuth2User) authentication.getPrincipal()).getAttribute("id");
                            memberCacheService.evict(userId);
                        }
                    })
                .logoutSuccessUrl("/"));

        return http.build();
    }
}
