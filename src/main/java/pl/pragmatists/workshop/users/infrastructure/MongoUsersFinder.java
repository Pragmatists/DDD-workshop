package pl.pragmatists.workshop.users.infrastructure;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import pl.pragmatists.workshop.users.domain.UsersFinder;

@Component
public class MongoUsersFinder implements UsersFinder {

    private final MongoTemplate mongoTemplate;

    public MongoUsersFinder(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public UserProjection byId(String id) {
        return mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), UserProjection.class, "users");
    }
}
