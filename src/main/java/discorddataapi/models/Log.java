package discorddataapi.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Document(collection = "logs")
public class Log {
    @Id
    private String id;

    private String type;
    private String editedMessage;
    private String createdAt;
    private Author editer;
    private String messageId;
    private String channelId;

    public String getCreatedAt() {
        return createdAt;
    }

    public String getId() { return id; }

    public String getType() { return type; }

    public String getEditedMessage() { return editedMessage; }

    public Author getEditer() { return editer; }

    public String getMessageId() { return messageId; }

    public String getChannelId() { return channelId; }
}
