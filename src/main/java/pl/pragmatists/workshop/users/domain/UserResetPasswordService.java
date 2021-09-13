package pl.pragmatists.workshop.users.domain;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserResetPasswordService {

    private final UserPasswordTokenRepository userPasswordTokenRepository;
    private final UserRepository userRepository;

    private UserResetPasswordService(UserPasswordTokenRepository userPasswordTokenRepository, UserRepository userRepository) {
        this.userPasswordTokenRepository = userPasswordTokenRepository;
        this.userRepository = userRepository;
    }

    public void resetPasswordFor(String userId, String token, String password) {
        Optional<UserPasswordToken> tokenCandidate = userPasswordTokenRepository.load(token);
        if (!tokenCandidate.isPresent()) {
            throw new InvalidUserTokenException();
        }
        User user = userRepository.load(userId);
        user.resetPassword(password);
        userRepository.save(user);
        userPasswordTokenRepository.delete(token);
    }
}
