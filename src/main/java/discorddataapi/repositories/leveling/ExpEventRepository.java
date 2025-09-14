package discorddataapi.repositories.leveling;

import discorddataapi.models.ExpEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ExpEventRepository extends MongoRepository<ExpEvent, String> {
    List<ExpEvent> findByUserId(String userId);

    List<ExpEvent> findByUserIdAndReasonAndTimestampBetween(
            String userId,
            String reason,
            Instant from,
            Instant to
    );

    List<ExpEvent> findByUserIdAndReasonAndTimestampLessThanEqual(String userId, String reason, Instant to);
}

