package com.education.eduprime;

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
public class UserPresenterTests {
    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Test
    public void getAllUsers() throws Exception {
        String uri = "http://localhost:" + port + "/api/v1/users";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test
    public void getDetailFoundUsers() throws Exception {
        User user = new User();
        user.setUserName("Andrew");
        user.setAddress("Yogyakarta");
        user.setAge(30);

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
        User user = new User();
        user.setUserName("Andrew");
        user.setAddress("Yogyakarta");
        user.setAge(30);

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
}
