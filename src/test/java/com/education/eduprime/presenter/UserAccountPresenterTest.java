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
public class UserAccountPresenterTest {
    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Test
    public void createNewUserAccountWithFoundUserIds() {
        String uri = "http://localhost:" + port + "/api/v1/user-accounts";
        RestTemplate restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateErrorResponseHandler())
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        User user = new User();
        user.setUserName("Andrew");
        user.setAddress("Yogyakarta");
        user.setAge(30);

        userRepository.save(user);

        // Setup payload
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("user_id", user.getId());
        userJsonObject.put("balances", 1800000);

        HttpEntity<String> request =
                new HttpEntity<String>(userJsonObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                uri, request, String.class);

        // Assertion
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void createNewUserAccountWithNotFoundUserIds() {
        String uri = "http://localhost:" + port + "/api/v1/user-accounts";
        RestTemplate restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateErrorResponseHandler())
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Random randomIds = new Random();

        // Setup payload
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("user_id", randomIds.nextInt());
        userJsonObject.put("balances", 1800000);

        HttpEntity<String> request =
                new HttpEntity<String>(userJsonObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                uri, request, String.class);

        // Assertion
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

}