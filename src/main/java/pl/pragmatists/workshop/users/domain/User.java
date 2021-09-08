package pl.pragmatists.workshop.users.domain;

public class User {
    public String id;
    public String email;

    public User(String id, String email, String password) {
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
}
