package pl.pragmatists.workshop.users.infrastructure;

import org.springframework.stereotype.Component;
import pl.pragmatists.workshop.users.domain.User;
import pl.pragmatists.workshop.users.domain.UserRepository;

import java.util.ArrayList;

@Component
public class InMemoryUserRepository implements UserRepository {


    private ArrayList<User> users = new ArrayList<>();

    @Override
    public void save(User user) {
        users.add(user);
    }

    @Override
    public User load(String id) {
        return users.stream().filter(user -> user.id.equals(id))
                .findFirst()
                .get();
    }
}
