package discorddataapi.repositories.leveling;

import discorddataapi.models.UserExp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExpRepository extends MongoRepository<UserExp, String> {
    UserExp findByUserId(String userId);
}
