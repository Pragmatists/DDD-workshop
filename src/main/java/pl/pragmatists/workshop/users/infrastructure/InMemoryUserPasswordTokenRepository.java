package pl.pragmatists.workshop.users.infrastructure;

import org.springframework.stereotype.Component;
import pl.pragmatists.workshop.users.domain.UserPasswordToken;
import pl.pragmatists.workshop.users.domain.UserPasswordTokenRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserPasswordTokenRepository implements UserPasswordTokenRepository {

    private List<UserPasswordToken> tokens = new ArrayList<>();

    @Override
    public void save(UserPasswordToken token) {
        tokens.add(token);
    }

    @Override
    public Optional<UserPasswordToken> load(String token) {
        return tokens.stream().filter($ -> $.token().equals(token)).findFirst();
    }
}
