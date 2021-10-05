package com.education.eduprime.presenter;

import com.education.eduprime.model.User;
import com.education.eduprime.repository.UserRepository;
import com.education.eduprime.utils.ResourceNotFoundException;
import com.education.eduprime.utils.RestTemplateErrorResponseHandler;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserPresenterTest {
    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Test
    public void getAllUsersWithoutPagination() throws Exception {
        User userAndrew = new User("Andrew", 30, "Sleman");
        User userAndic = new User("Andic", 30, "Yogya");

        userRepository.save(userAndrew);
        userRepository.save(userAndic);

        String uri = "http://localhost:" + port + "/api/v1/users";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test
    public void getAllUsersWithPagination() {
        User user1 = new User("Andrew", 20, "Jakarta");
        User user2 = new User("Andrew", 20, "Jakarta");
        User user3 = new User("Andrew", 20, "Jakarta");
        User user4 = new User("Andrew", 20, "Jakarta");
        User user5 = new User("Andrew", 20, "Jakarta");


        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);

        String uri = "http://localhost:" + port + "/api/v1/users?" + "page=1&size=10";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test
    public void getDetailFoundUsers() throws Exception {
        User user = new User("Andrew", 20, "Jakarta");

        userRepository.save(user);

        String uri = "http://localhost:" + port + "/api/v1/users/"+ user.getId();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getDetailNotFoundUsers() throws ResourceNotFoundException {
        Random randomIds = new Random();
        String uri = "http://localhost:" + port + "/api/v1/users/" + randomIds.nextInt();
        RestTemplate restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateErrorResponseHandler())
                .build();
        restTemplate.getForEntity(uri, String.class);
    }

    @Test
    public void createNewUser() {
        String uri = "http://localhost:" + port + "/api/v1/users";
        RestTemplate restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateErrorResponseHandler())
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Setup payload
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("name", "John");
        userJsonObject.put("address", "Yogyakarta");
        userJsonObject.put("age", 28);

        HttpEntity<String> request =
                new HttpEntity<String>(userJsonObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                uri, request, String.class);

        // Assertion
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test
    public void updateFoundUser() {
        User user = new User("Andrew", 20, "Jakarta");

        userRepository.save(user);

        String uri = "http://localhost:" + port + "/api/v1/users/" + user.getId();
        RestTemplate restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateErrorResponseHandler())
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Setup payload
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("name", "John Mir");
        userJsonObject.put("address", "California");
        userJsonObject.put("age", 28);

        HttpEntity<String> request =
                new HttpEntity<String>(userJsonObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                uri, HttpMethod.PUT, request, String.class);

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateNotFoundUser() throws ResourceNotFoundException {
        Random randomIds = new Random();
        String uri = "http://localhost:" + port + "/api/v1/users/" + randomIds.nextInt();
        RestTemplate restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateErrorResponseHandler())
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Setup payload
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("name", "John Mir");
        userJsonObject.put("address", "California");
        userJsonObject.put("age", 28);

        HttpEntity<String> request =
                new HttpEntity<String>(userJsonObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                uri, HttpMethod.PUT, request, String.class);

        // Assertion
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test
    public void deleteFoundUser() {
        User user = new User("Andrew", 20, "Jakarta");

        userRepository.save(user);

        String uri = "http://localhost:" + port + "/api/v1/users/" + user.getId();
        RestTemplate restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateErrorResponseHandler())
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = restTemplate.exchange(
                uri, HttpMethod.DELETE, null, String.class);

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteNotFoundUser() throws ResourceNotFoundException {
        Random randomIds = new Random();
        String uri = "http://localhost:" + port + "/api/v1/users/" + randomIds.nextInt();
        RestTemplate restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateErrorResponseHandler())
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = restTemplate.exchange(
                uri, HttpMethod.DELETE, null, String.class);

        // Assertion
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }
}
