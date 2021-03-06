package pl.pragmatists.workshop.users;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class UserOperationsTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	void setUp() {
		mongoTemplate.dropCollection("users");
	}

	@Test
	void create_for_email() {
		ResponseEntity<UserFetchResponse> response = restTemplate.postForEntity("/users", new UserCreationRequest("jan.przykladowy@pragmatists.pl"), UserFetchResponse.class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody().id).isNotNull();
	}

	@Test
	void create_and_fetch() {
		ResponseEntity<UserFetchResponse> response = restTemplate.postForEntity("/users", new UserCreationRequest("jan.przykladowy@pragmatists.pl"), UserFetchResponse.class);

		String userId = response.getBody().id;
		ResponseEntity<UserFetchResponse> fetchResponse = restTemplate.getForEntity("/users/" + userId, UserFetchResponse.class);

		assertThat(fetchResponse.getStatusCode()).isEqualTo(OK);
		UserFetchResponse responseBody = fetchResponse.getBody();
		assertThat(responseBody.id).isNotNull();
		assertThat(responseBody.email).isEqualTo("jan.przykladowy@pragmatists.pl");
	}

	@Test
	void fail_creating_when_email_not_valid() {
		ResponseEntity<UserFetchResponse> response = restTemplate.postForEntity("/users", new UserCreationRequest("jan.bezmalpy"), UserFetchResponse.class);

		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
	}

	@Test
	void fail_creating_when_password_not_strong_enough() {
		ResponseEntity<UserFetchResponse> response = restTemplate.postForEntity("/users", new UserCreationRequest("jan.przykladowy@pragmatists.pl", "123456"), UserFetchResponse.class);

		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
	}

	@Test
	void fail_creating_when_user_already_exists() {
		restTemplate.postForEntity("/users", new UserCreationRequest("jan.przykladowy@pragmatists.pl"), UserFetchResponse.class);

		ResponseEntity<UserFetchResponse> response = restTemplate.postForEntity("/users", new UserCreationRequest("jan.przykladowy@pragmatists.pl"), UserFetchResponse.class);

		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
	}

	@Test
	void login() {
		createUser("jan.przykladowy@pragmatists.pl", "some-password");

		ResponseEntity<Void> loginResponse = restTemplate.postForEntity("/login", new UserLoginRequest("jan.przykladowy@pragmatists.pl", "some-password"), Void.class);

		assertThat(loginResponse.getStatusCode()).isEqualTo(OK);
	}

	@Test
	void fail_login_with_wrong_password() {
		createUser("jan.przykladowy@pragmatists.pl", "some-password");

		ResponseEntity<Void> loginResponse = restTemplate.postForEntity("/login", new UserLoginRequest("jan.przykladowy@pragmatists.pl", "some-password2"), Void.class);

		assertThat(loginResponse.getStatusCode()).isEqualTo(UNAUTHORIZED);
	}

	@Test
	void reset_password_to_new_one() {
		String userId = createUser("jan.przykladowy@pragmatists.pl", "some-password");

		ResponseEntity<UserRemindPasswordResponse> response = restTemplate.postForEntity(format("/users/%s/remindPassword", userId), new HashMap<>(), UserRemindPasswordResponse.class);

		String token = response.getBody().token;
		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(token).isNotNull();

		ResponseEntity<Void> resetPasswordResponse = restTemplate.postForEntity(format("/users/%s/resetPassword", userId),
				new UserResetPasswordRequest("new-password", token), Void.class);

		assertThat(resetPasswordResponse.getStatusCode()).isEqualTo(OK);
		assertThat(loginUser("jan.przykladowy@pragmatists.pl", "some-password").getStatusCode()).isEqualTo(UNAUTHORIZED);
		assertThat(loginUser("jan.przykladowy@pragmatists.pl", "new-password").getStatusCode()).isEqualTo(OK);
	}

	@Test
	void fail_resetting_password_twice_with_same_token() {
		String userId = createUser("jan.przykladowy@pragmatists.pl", "some-password");
		ResponseEntity<UserRemindPasswordResponse> response = restTemplate.postForEntity(format("/users/%s/remindPassword", userId), new HashMap<>(), UserRemindPasswordResponse.class);
		String token = response.getBody().token;

		ResponseEntity<Void> firstResetPasswordResponse = restTemplate.postForEntity(format("/users/%s/resetPassword", userId),
				new UserResetPasswordRequest("new-password", token), Void.class);
		ResponseEntity<Void> secondResetPasswordResponse = restTemplate.postForEntity(format("/users/%s/resetPassword", userId),
				new UserResetPasswordRequest("new-password-2", token), Void.class);

		assertThat(firstResetPasswordResponse.getStatusCode()).isEqualTo(OK);
		assertThat(secondResetPasswordResponse.getStatusCode()).isEqualTo(BAD_REQUEST);
	}

	@Test
	void fail_resetting_password_with_invalid_token() {
		String userId = createUser("jan.przykladowy@pragmatists.pl", "some-password");

		ResponseEntity<Void> resetPasswordResponse = restTemplate.postForEntity(format("/users/%s/resetPassword", userId),
				new UserResetPasswordRequest("new-password", "token"), Void.class);

		assertThat(resetPasswordResponse.getStatusCode()).isEqualTo(BAD_REQUEST);
	}

	@Test
	void block_user() {
		String userId = createUser("jan.przykladowy@pragmatists.pl", "some-password");

		ResponseEntity<Void> blockResponse = restTemplate.postForEntity(format("/users/%s/block", userId), new HashMap<>(), Void.class);

		assertThat(blockResponse.getStatusCode()).isEqualTo(OK);
		assertThat(loginUser("jan.przykladowy@pragmatists.pl", "some-password").getStatusCode()).isEqualTo(UNAUTHORIZED);
	}

	@Test
	void unblock_user() {
		String userId = createUser("jan.przykladowy@pragmatists.pl", "some-password");
		blockUser(userId);

		ResponseEntity<Void> unblockResponse = restTemplate.postForEntity(format("/users/%s/unblock", userId), new HashMap<>(), Void.class);

		assertThat(unblockResponse.getStatusCode()).isEqualTo(OK);
		assertThat(loginUser("jan.przykladowy@pragmatists.pl", "some-password").getStatusCode()).isEqualTo(UNAUTHORIZED);
	}

	private ResponseEntity<Void> resetPassword(String userId, String password, String token) {
		ResponseEntity<Void> resetPasswordResponse = restTemplate.postForEntity(format("/users/%s/resetPassword", userId), new UserResetPasswordRequest(password, token), Void.class);
		return resetPasswordResponse;
	}

	private void blockUser(String userId) {
		ResponseEntity<Void> blockResponse = restTemplate.postForEntity(format("/users/%s/block", userId), new HashMap<>(), Void.class);
		assertThat(blockResponse.getStatusCode()).isEqualTo(OK);
	}

	private ResponseEntity<Void> loginUser(String email, String password) {
		return restTemplate.postForEntity("/login", new UserLoginRequest(email, password), Void.class);
	}

	private String createUser(String email, String password) {
		ResponseEntity<UserFetchResponse> userCreationResponse = restTemplate.postForEntity("/users",
				new UserCreationRequest(email, password), UserFetchResponse.class);
		return userCreationResponse.getBody().id;
	}

	@Test
	void reset_user_password() {

	}

	public static class UserCreationRequest {

		public final String email;
		public final String password;

		public UserCreationRequest(String email) {
			this.email = email;
			this.password = RandomStringUtils.random(10);
		}

		public UserCreationRequest(String email, String password) {
			this.email = email;
			this.password = password;
		}
	}

	public static class UserFetchResponse {
		public String id;
		public String email;
	}

	private static class UserLoginRequest {
		public String email;
		public String password;

		public UserLoginRequest(String email, String password) {
			this.email = email;
			this.password = password;
		}
	}

	private static class UserResetPasswordRequest {
		public String password;
		public String token;

		public UserResetPasswordRequest(String password, String token) {
			this.password = password;
			this.token = token;
		}
	}

	private static class UserRemindPasswordResponse {
		public String token;
	}
}
