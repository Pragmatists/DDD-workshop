package pl.pragmatists.workshop.users.infrastructure;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import pl.pragmatists.workshop.users.domain.User;
import pl.pragmatists.workshop.users.domain.UserRepository;

@Component
public class MongoUserRepository implements UserRepository {

    private final MongoTemplate mongoTemplate;

    public MongoUserRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void save(User user) {
        mongoTemplate.save(user, "users");
    }

    @Override
    public User load(String id) {
        return mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), User.class, "users");
    }

    @Override
    public User find(String email) {
        return mongoTemplate.findOne(Query.query(Criteria.where("email.email").is(email)), User.class, "users");
    }
}
