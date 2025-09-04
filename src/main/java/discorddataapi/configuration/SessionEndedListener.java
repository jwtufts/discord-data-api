package discorddataapi.configuration;

import discorddataapi.services.MemberCacheService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class SessionEndedListener implements ApplicationListener<SessionDestroyedEvent> {

    private final MemberCacheService memberCacheService;

    public SessionEndedListener(MemberCacheService memberCacheService) {
        this.memberCacheService = memberCacheService;
    }

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        event.getSecurityContexts().forEach(context -> {
            Object principal = context.getAuthentication().getPrincipal();
            if (principal instanceof OAuth2User oAuth2User) {
                String userId = oAuth2User.getAttribute("id");
                memberCacheService.evict(userId);
            }
        });
    }
}

