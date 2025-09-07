package discorddataapi.services;

import discorddataapi.models.Author;
import discorddataapi.models.Message;
import discorddataapi.repositories.MessageRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Service
public class ActiveUsersService {
    private final MessageRepository messageRepository;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private List<Author> cachedAuthors = new ArrayList<>();

    public ActiveUsersService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Refresh every hour (3600000 ms)
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void refreshCache() {
        List<Message> messages = messageRepository.findAll();

        // sort newest first, deduplicate by userId
        Map<String, Author> latestByUser = messages.stream()
                .filter(msg -> msg.getAuthor() != null && !msg.isBot())
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .collect(Collectors.toMap(
                        msg -> msg.getAuthor().getUserId(),
                        Message::getAuthor,
                        (a1, a2) -> a1,
                        LinkedHashMap::new
                ));

        lock.writeLock().lock();
        try {
            cachedAuthors = new ArrayList<>(latestByUser.values());
            System.out.println("Refreshed active users (" + cachedAuthors.size() + " message authors)");
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<Author> getAuthors() {
        lock.readLock().lock();
        try {
            return cachedAuthors;
        } finally {
            lock.readLock().unlock();
        }
    }
}
