package pl.pragmatists.workshop.users.domain;

import org.apache.commons.lang3.RandomStringUtils;

public class UserPasswordToken {

    private final String id;
    private final String userId;
    private final String token;

    public UserPasswordToken(String id, String userId) {
        this.id = id;
        this.userId = userId;
        this.token = RandomStringUtils.random(10);
    }

    public String token() {
        return token;
    }
}
