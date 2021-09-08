package pl.pragmatists.workshop.users.domain;

import org.springframework.stereotype.Component;

public class User {

    public String id;
    public String email;

    private User(String id, String email, String password) {
        validateEmail(email);
        validatePassword(password);
        this.id = id;
        this.email = email;
    }

    private void validateEmail(String email) {
        if (!email.contains("@")) {
            throw new EmailNotValidException();
        }
    }

    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new PasswordNotValidException();
        }
    }

    public static class EmailNotValidException extends ValidationException {
    }

    public static class PasswordNotValidException extends ValidationException {
    }

    private static class UserAlreadyExistsException extends ValidationException {
    }

    @Component
    public static class UserFactory {

        private final IdGenerator idGenerator;
        private final Users users;

        public UserFactory(IdGenerator idGenerator, Users users) {
            this.idGenerator = idGenerator;
            this.users = users;
        }

        public User newUser(String email, String password) {
            if (users.hasUserWith(email)) {
                throw new UserAlreadyExistsException();
            }
            User user = new User(idGenerator.id(), email, password);
            return user;
        }

    }

}
