package discorddataapi.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.OffsetDateTime;

@Data
@Document(collection = "messages")
public class Message {
    @Id
    private String id;

    private String channelId;
    private String content;
    private String messageId;
    private Author author;
    private Boolean bot;
    private Instant createdAt;
    private Boolean edited;
    private Boolean deleted;

    public Boolean isBot() {
        return bot;
    }
}
