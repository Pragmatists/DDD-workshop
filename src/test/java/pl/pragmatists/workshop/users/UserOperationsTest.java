package pl.pragmatists.workshop.users;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import pl.pragmatists.workshop.users.domain.TestUserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class UserOperationsTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private TestUserRepository userRepository;

	@BeforeEach
	void setUp() {
		userRepository.clearAll();
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
}
