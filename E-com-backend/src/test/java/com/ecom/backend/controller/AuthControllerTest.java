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
    private User user;

    @BeforeEach
    void setUp() {

    }

    @BeforeAll
    void setup(){
        baseUrl = "/api/auth";
        userRepository.deleteAll();

        this.user = new User();
        user.setUserName("testuser1");
        user.setPassword("password123");
        user.setRole("pakaya");
    }

    @Test
    void testRegister() {
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/register", user, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
    }

    @Test
    void testLoginWithValidCredentials() {
        User userHere = new User();
        userHere.setUserName("testuser2");
        userHere.setPassword("password123");
        userHere.setRole("pakaya");
        ResponseEntity<String> getResponse = restTemplate.postForEntity(baseUrl + "/register", userHere, String.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("User registered successfully", getResponse.getBody());

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/login", userHere, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody()); // JWT token
        System.out.println(response.getBody());
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
}
