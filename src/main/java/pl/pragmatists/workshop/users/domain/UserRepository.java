package pl.pragmatists.workshop.users.domain;

public interface UserRepository {
    void save(User user);

    User load(String id);

    User find(String email);
}
