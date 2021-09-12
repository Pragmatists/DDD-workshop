package pl.pragmatists.workshop.users.application;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pragmatists.workshop.users.domain.*;

@RestController
public class UsersEndpoint {

    private final UserRepository userRepository;
    private final User.UserFactory userFactory;
    private final UserPasswordTokenRepository userPasswordTokenRepository;
    private final IdGenerator idGenerator;
    private final UserResetPasswordService userResetPasswordService;

    public UsersEndpoint(UserRepository userRepository, User.UserFactory userFactory, UserPasswordTokenRepository userPasswordTokenRepository, IdGenerator idGenerator, UserResetPasswordService userResetPasswordService) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.userPasswordTokenRepository = userPasswordTokenRepository;
        this.idGenerator = idGenerator;
        this.userResetPasswordService = userResetPasswordService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserCreationResponseJson> createUser(@RequestBody UserCreationJson userCreationJson) {
        User user = userFactory.newUser(userCreationJson.email, userCreationJson.password);
        userRepository.save(user);
        return ResponseEntity.ok(new UserCreationResponseJson(user.id));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserFetchResponseJson> fetchUser(@PathVariable String id) {
        User user = userRepository.load(id);
        return ResponseEntity.ok(new UserFetchResponseJson(user));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserLoginJson userLoginJson) {
        User user = userRepository.find(userLoginJson.email);
        if (!user.canLoginWith(userLoginJson.password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{id}/block")
    public ResponseEntity<Void> block(@PathVariable("id") String id) {
        User user = userRepository.load(id);
        user.block();
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{id}/unblock")
    public ResponseEntity<Void> unblock(@PathVariable("id") String id) {
        User user = userRepository.load(id);
        user.unblock();
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{userId}/remindPassword")
    public ResponseEntity<UserRemindPasswordResponseJson> remindPassword(@PathVariable String userId) {
        UserPasswordToken userPasswordToken = new UserPasswordToken(idGenerator.id(), userId);
        userPasswordTokenRepository.save(userPasswordToken);
        return ResponseEntity.ok(new UserRemindPasswordResponseJson(userPasswordToken.token()));
    }

    @PostMapping("/users/{id}/resetPassword")
    public ResponseEntity<Void> resetPassword(@PathVariable("id") String userId, @RequestBody UserResetPasswordRequestJson userResetPasswordRequestJson) {
        userResetPasswordService.resetPasswordFor(userId, userResetPasswordRequestJson.token, userResetPasswordRequestJson.password);
        return ResponseEntity.ok().build();
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
            this.email = user.email.value();
        }
    }

    private static class UserLoginJson {
        public String email;
        public String password;
    }

    private static class UserResetPasswordRequestJson {
        public String password;
        public String token;
    }

    private static class UserRemindPasswordResponseJson {
        public String token;

        public UserRemindPasswordResponseJson(String token) {
            this.token = token;
        }
    }

}
