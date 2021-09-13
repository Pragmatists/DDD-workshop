package pl.pragmatists.workshop.users.infrastructure;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import pl.pragmatists.workshop.users.domain.User;
import pl.pragmatists.workshop.users.domain.Users;

@Component
public class MongoUsers implements Users {

    private final MongoTemplate mongoTemplate;

    public MongoUsers(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean hasUserWith(String email) {
        return mongoTemplate.exists(Query.query(Criteria.where("email.email").is(email)), User.class, "users");
    }
}
