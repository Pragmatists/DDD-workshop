package pl.pragmatists.workshop.users.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pragmatists.workshop.users.domain.IdGenerator;
import pl.pragmatists.workshop.users.domain.User;

@RestController
public class UsersEndpoint {

    private final IdGenerator idGenerator;

    public UsersEndpoint(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@RequestBody UserCreationJson userCreationJson) {
        User user = new User(idGenerator.id(), userCreationJson.email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Void> fetchUser(@PathVariable String id) {
        return ResponseEntity.ok().build();
    }

    public static class UserCreationJson {

        public String email;
    }
}
