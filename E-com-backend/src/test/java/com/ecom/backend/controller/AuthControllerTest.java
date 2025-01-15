package com.ecom.backend.controller;

import com.ecom.backend.model.User;
import com.ecom.backend.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Allows non-static @BeforeAll
/*
This annotation tells JUnit to create a single test instance for the entire test class,
enabling the use of non-static @BeforeAll methods.
 */
public class AuthControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @BeforeAll
    void setup(){
        baseUrl = "/api/auth";
    }

    @Test
    void testRegister() {
        User user = new User();
        user.setUserName("testuser1");
        user.setPassword("password123");
        user.setRole("pakaya");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/register", user, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
    }

    @Test
    void testLoginWithValidCredentials() {
        User user = new User();
        user.setUserName("testuser2");
        user.setPassword("password123");
        user.setRole("pakaya");
        ResponseEntity<String> getResponse = restTemplate.postForEntity(baseUrl + "/register", user, String.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("User registered successfully", getResponse.getBody());

        user.setRole(null);
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/login", user, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody()); // JWT token
    }

    @Test
    void testLoginWithCorrectUserNameWrongPW() {
        User user = new User();
        user.setUserName(user.getUserName());
        user.setPassword("wrongPassword");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/login", user, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
    }

    @Test
    void testLoginWithInvalidCredentials() {
        User user = new User();
        user.setUserName("invalidUser");
        user.setPassword("wrongPassword");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/login", user, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
    }

    @Test
    void deleteWithValidCredentials(){
        User userhere = new User();
        userhere.setUserName("testUserForDelete");
        userhere.setPassword("password123");
        userhere.setRole("pakaya");
        ResponseEntity<String> getResponse = restTemplate.postForEntity(baseUrl + "/register", userhere, String.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        userhere.setRole(null);
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/login", userhere, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        String token = response.getBody();

        HttpHeaders headers = createHeaders(token);
        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers);
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(baseUrl, HttpMethod.DELETE, deleteRequest, Void.class);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        ResponseEntity<String> reloginResponse = restTemplate.postForEntity(baseUrl + "/login", userhere, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, reloginResponse.getStatusCode());
    }

    private HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
}
