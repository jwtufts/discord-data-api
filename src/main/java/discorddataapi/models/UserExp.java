package discorddataapi.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
@Document(collection = "users_exp")
public class UserExp {
    @Id
    private String id;

    @Field("user_id")
    private String userId;

    private int daily;

    private int monthly;

    private String nickname;

    private int total;

    private int unboosted;

    private String username;

    private int weekly;

    private int yearly;
}
