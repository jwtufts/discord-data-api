package discorddataapi.services;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MemberCacheService {
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public void put(String userId, String memberJson) {
        cache.put(userId, memberJson);
    }

    public String get(String userId) {
        return cache.get(userId);
    }

    public void evict(String userId) {
        cache.remove(userId);
    }

    public void clear() {
        cache.clear();
    }
}
