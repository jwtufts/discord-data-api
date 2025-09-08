package discorddataapi.repositories;

import discorddataapi.models.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    Message findByMessageId(String messageId);
    List<Message> findByAuthorUserIdAndCreatedAtBetween(String userId, Instant from, Instant to);
}
