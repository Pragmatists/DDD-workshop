package pl.pragmatists.workshop.users.domain;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

    public final String id;
    public final Email email;
    private final Password password;

    private User(String id, String email, String password) {
        this.id = id;
        this.password = new Password(password);
        this.email = new Email(email);
    }

    public boolean passwordMatches(String password) {
        return this.password.matches(password);
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

    private static class Password {

        private final String password;

        public Password(String password) {
            validate(password);
            this.password = cipher(password);
        }

        private String cipher(String password) {
            try {
                return new String(MessageDigest.getInstance("SHA-1").digest(password.getBytes()));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean matches(String password) {
            return this.password.equals(cipher(password));
        }

        private void validate(String password) {
            if (password.length() < 8) {
                throw new PasswordNotValidException();
            }
        }
    }

    public static class Email {
        private String email;

        public Email(String email) {
            validate(email);
            this.email = email;
        }

        private void validate(String email) {
            if (!email.contains("@")) {
                throw new EmailNotValidException();
            }
        }

        public String value() {
            return email;
        }
    }
}
