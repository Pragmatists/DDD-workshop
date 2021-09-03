package pl.pragmatists.workshop.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class UserOperationsTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void create_for_email() {
		ResponseEntity<String> response = restTemplate.postForEntity("/users", new UserCreationRequest("jan.przykladowy@pragmatists.pl"), String.class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
	}

	@Test
	void create_and_fetch() {
		ResponseEntity<String> response = restTemplate.postForEntity("/users", new UserCreationRequest("jan.przykladowy@pragmatists.pl"), String.class);

		String userId = response.getBody();
		ResponseEntity<UserFetchResponse> fetchResponse = restTemplate.getForEntity("/users/" + userId, UserFetchResponse.class);

		assertThat(fetchResponse.getStatusCode()).isEqualTo(OK);
		UserFetchResponse responseBody = fetchResponse.getBody();
		assertThat(responseBody.id).isNotNull();
		assertThat(responseBody.email).isEqualTo("jan.przykladowy@pragmatists.pl");
	}

	public static class UserCreationRequest {

		public String email;

		public UserCreationRequest(String email) {
			this.email = email;
		}
	}

	public static class UserFetchResponse {
		public String id;
		public String email;
	}
}
