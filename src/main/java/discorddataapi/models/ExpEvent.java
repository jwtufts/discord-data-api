package discorddataapi.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
@Document(collection = "exp_events")
public class ExpEvent {
    @Id
    private String id;

    @Field("user_id")
    private String userId;

    @Field("exp")
    private int exp;

    @Field("unboosted")
    private int unboosted;

    @Field("timestamp")
    private Instant timestamp;

    @Field("reason")
    private String reason;

    @Field("elligible_users")
    private int eligibleUsers;

    @Field("timeInVoice")
    private Integer timeInVoice;

    @Field("time")
    @Transient
    private Integer legacyTime;

    @Field("channel_id")
    private String channelId;

    @Field("message_id")
    private String messageId;

    public Integer getTime() {
        return timeInVoice != null ? timeInVoice : legacyTime;
    }

    public void setTime(Integer value) {
        this.timeInVoice = value;
    }
}
