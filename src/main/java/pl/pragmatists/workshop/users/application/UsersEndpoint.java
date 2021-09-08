package pl.pragmatists.workshop.users.application;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pragmatists.workshop.users.domain.IdGenerator;
import pl.pragmatists.workshop.users.domain.User;
import pl.pragmatists.workshop.users.domain.UserRepository;
import pl.pragmatists.workshop.users.domain.ValidationException;

@RestController
public class UsersEndpoint {

    private final IdGenerator idGenerator;
    private final UserRepository userRepository;

    public UsersEndpoint(IdGenerator idGenerator, UserRepository userRepository) {
        this.idGenerator = idGenerator;
        this.userRepository = userRepository;
    }

    @PostMapping("/users")
    public ResponseEntity<UserCreationResponseJson> createUser(@RequestBody UserCreationJson userCreationJson) {
        User user = new User(idGenerator.id(), userCreationJson.email, userCreationJson.password);
        userRepository.save(user);
        return ResponseEntity.ok(new UserCreationResponseJson(user.id));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserFetchResponseJson> fetchUser(@PathVariable String id) {
        User user = userRepository.load(id);
        return ResponseEntity.ok(new UserFetchResponseJson(user));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public void validationError() {

    }

    private static class UserCreationJson {

        public String email;
        public String password;
    }

    private static class UserCreationResponseJson {

        public String id;

        public UserCreationResponseJson(String id) {
            this.id = id;
        }
    }

    private static class UserFetchResponseJson {

        public final String id;
        public final String email;

        public UserFetchResponseJson(User user) {
            this.id = user.id;
            this.email = user.email;
        }
    }

}
