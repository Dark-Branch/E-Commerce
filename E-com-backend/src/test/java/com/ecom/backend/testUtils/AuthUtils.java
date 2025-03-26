package com.ecom.backend.testUtils;

import com.ecom.backend.dto.LoginRequest;
import com.ecom.backend.dto.SignupRequest;
import com.ecom.backend.model.User;
import com.ecom.backend.service.AuthService;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthUtils {
    public static String setupSignedUserAndGetToken(User user, TestRestTemplate restTemplate) {
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "/api/auth/login",
                LoginRequest.builder()
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .build(),
                String.class
        );
        assertThat(loginResponse.getBody()).isNotNull();
        return extractToken(loginResponse.getBody());
    }

    public static User saveUser(User user, AuthService authService, String email){
        user = new User();
        user.setUserName("test_user");
        user.setEmail(email);
        // need to use in login req
        String password = "password";
        user.setPassword(password);

        SignupRequest request = SignupRequest.builder()
                .email(user.getEmail())
                .name(user.getUserName())
                .password(user.getPassword())
                .build();
        user = authService.registerUser(request);
        // this changes when saving
        user.setPassword(password);
        return user;
    }

    public static HttpHeaders getHttpHeadersWithToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    static String extractToken(String responseBody) {
        return responseBody.split("\"token\":\"")[1].split("\"")[0];
    }
}
