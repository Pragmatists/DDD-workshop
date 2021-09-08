package pl.pragmatists.workshop.users.infrastructure;

import org.springframework.stereotype.Component;
import pl.pragmatists.workshop.users.domain.TestUserRepository;
import pl.pragmatists.workshop.users.domain.User;
import pl.pragmatists.workshop.users.domain.UserRepository;
import pl.pragmatists.workshop.users.domain.Users;

import java.util.ArrayList;

@Component
public class InMemoryUserRepository implements UserRepository, Users, TestUserRepository {

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

    @Override
    public boolean hasUserWith(String email) {
        return users.stream().anyMatch(user -> user.email.equals(email));
    }

    @Override
    public void clearAll() {
        users.clear();
    }
}
