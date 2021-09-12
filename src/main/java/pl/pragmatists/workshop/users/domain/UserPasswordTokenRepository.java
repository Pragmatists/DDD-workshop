package pl.pragmatists.workshop.users.domain;

import java.util.Optional;

public interface UserPasswordTokenRepository {
    void save(UserPasswordToken userPasswordToken);

    Optional<UserPasswordToken> load(String token);
}
