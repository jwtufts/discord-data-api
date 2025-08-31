package discorddataapi.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Document(collection = "messages")
public class Message {
    @Id
    private String id;

    private String channelId;
    private String content;
    private String messageId;
    private Author author;
    private Boolean bot;
    private String createdAt;
    private Boolean edited;
    private Boolean deleted;

    public String getId() {
        return id;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getContent() {
        return content;
    }

    public String getMessageId() {
        return messageId;
    }

    public Author getAuthor() {
        return author;
    }

    public Boolean isBot() {
        return bot;
    }

    public OffsetDateTime getCreatedAt() {
       return createdAt != null ? OffsetDateTime.parse(createdAt) : null;
    }

    public Boolean isEdited() {
        return edited;
    }

    public Boolean isDeleted() {
        return deleted;
    }
}
