package discorddataapi.repositories;

import discorddataapi.models.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    Message findByMessageId(String messageId);
}
