package com.education.eduprime.presenter;

import com.education.eduprime.model.User;
import com.education.eduprime.repository.UserRepository;
import com.education.eduprime.utils.ResourceNotFoundException;
import com.education.eduprime.utils.RestTemplateErrorResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserPresenterTest {
    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Before
    public void initData() {
        List<User> users = Arrays.asList(
                new User("Andrew", 21, "Jakarta"),
                new User("Matthew", 22, "Banjarnegara"),
                new User("Anderson", 23, "Yogya"),
                new User("Sylvia", 22, "Solo"),
                new User("Carolina", 22, "Blora"));

        userRepository.saveAll(users);
    }

    @After
    public void clearData() {
        userRepository.deleteAll();
    }

    @Test
    public void getAllUsersWithoutPagination() throws Exception {
        String uri = "http://localhost:" + port + "/api/v1/users";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(5, jsonObject.getJSONArray("data").length());
    }

    @Test
    public void getAllUsersWithPagination() throws Exception {

        String uri = "http://localhost:" + port + "/api/v1/users?" + "page=0&size=10";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(5, jsonObject.getJSONArray("data").length());
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
    public void createNewUser() throws JSONException {
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
    public void updateFoundUser() throws JSONException {
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
    public void updateNotFoundUser() throws ResourceNotFoundException, JSONException {
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
