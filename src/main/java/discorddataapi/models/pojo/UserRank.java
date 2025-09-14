package discorddataapi.models.pojo;

import lombok.Data;

@Data
public class UserRank {
    private int daily;
    private int weekly;
    private int monthly;
    private int yearly;
    private int total;
    private int unboosted;
}
