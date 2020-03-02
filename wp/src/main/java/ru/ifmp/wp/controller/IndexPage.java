package ru.ifmp.wp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.ifmp.wp.model.Post;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class IndexPage {

    @Autowired
    DataSource dataSource;

    Logger logger = LoggerFactory.getLogger(getClass());
    private final RestTemplate restTemplate = new RestTemplateBuilder().build();


    @PostMapping("/hi")
    public void react() {
        System.out.println("Lol");
        logger.info("Lol");
    }

    private void send() {
        try{
            Connection connection = dataSource.getConnection();
            String selectSql = "select * from cards";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSql)) {

                // Print results from select statement
                System.out.println("Top 20 categories:");
                while (resultSet.next())
                {
                    System.out.println(resultSet.getString(1) + " "
                            + resultSet.getString(2));
                }
                connection.close();
            }
            System.out.println("conected");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @GetMapping("/hi")
    public String react2() {
        System.out.println("Kek");
        logger.error("Kek");

        send();
//        getPostsPlainJSON();
        return "Даня сел на ферзя";

    }

    private Post getPostsPlainJSON() {
        String url = "http://localhost:8081/hi";

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set content-type header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set accept header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a map for post parameters
        Map<String, Object> map = new HashMap<>();
        map.put("userId", 1);
        map.put("title", "Introduction to Spring Boot");
        map.put("body", "Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications.");

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<Post> response = this.restTemplate.postForEntity(url, entity, Post.class);

        // check response status code
        if (response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody();
        } else {
            return null;
        }
    }


}
