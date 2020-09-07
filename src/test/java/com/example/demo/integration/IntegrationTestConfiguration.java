package com.example.demo.integration;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class IntegrationTestConfiguration {

    private static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

    @LocalServerPort
    protected int port;
    protected String basePath;

    @Autowired
    protected DatabaseCleaner databaseCleaner;

    public void setUp() {
        databaseCleaner.clearTables();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
    }

    protected void setBasePath(String basePath) {
        this.basePath = basePath;
        RestAssured.basePath = basePath;
    }

    protected String getIdHeaderLocation(Response response) {
        String location = response.getHeader("Location");
        Matcher matcher = UUID_PATTERN.matcher(location);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

}
