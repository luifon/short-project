package com.example.demo.integration.entity.movie;

import com.example.demo.domains.movies.dtos.MovieCreateDTO;
import com.example.demo.domains.movies.entity.Movie;
import com.example.demo.domains.movies.repository.MovieRepository;
import com.example.demo.integration.IntegrationTestConfiguration;
import com.example.demo.utils.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class MovieIT extends IntegrationTestConfiguration {

    @Autowired
    MovieRepository movieRepository;
    private UUID movieById;
    private String movieCreate;

    @Before
    public void setup() {
        super.setUp();
        RestAssured.basePath = "/movies";
        prepareData();
        movieCreate = ResourceUtils.getContentFromResource("/com/example/demo/integration/json/movie-create.json");
    }

    private void prepareData() {
        MovieCreateDTO movieCreateDTO = new MovieCreateDTO("NEW MOVIE 1", "THIS IS A MOVIE ABOUT AN INTEGRATION TEST", 5, LocalDateTime.now());
        movieRepository.save(new Movie(movieCreateDTO));
        MovieCreateDTO movieCreateDTO2 = new MovieCreateDTO("NEW MOVIE 2", "THIS IS YET ANOTHER MOVIE ABOUT AN INTEGRATION TEST", 10, LocalDateTime.now());
        movieById = movieRepository.save(new Movie(movieCreateDTO2)).getId();
    }

    @Test
    public void findAll_test() {
        given().get().then()
                .body("size()", is(2))
                .body("[0].title", is("NEW MOVIE 1"))
                .body("[0].userRating", is(5))
                .body("[1].title", is("NEW MOVIE 2"))
                .body("[1].userRating", is(10));
    }


    @Test
    public void findById_test() {
        given()
                .pathParam("id", movieById)
                .get("/{id}").then()
                .body("title", is("NEW MOVIE 2"))
                .body("synopsis", is("THIS IS YET ANOTHER MOVIE ABOUT AN INTEGRATION TEST"))
                .body("userRating", is(10));
    }

    @Test
    public void delete_test() {
        given()
                .pathParam("id", movieById)
                .contentType(ContentType.JSON)
                .when()
                .delete("/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void findById_notFound_test() {
        given()
                .pathParam("id", UUID.randomUUID())
                .get("/{id}").then()
                .body("message", is("MOVIE NOT FOUND."));
    }

    @Test
    public void create_test() {
        String createJson = movieCreate
                .replace("{{title}}", "CREATE TEST TITLE")
                .replace("{{synopsis}}", "CREATE TEST SYNOPSIS")
                .replace("{{userRating}}", "5")
                .replace("{{releaseDate}}", LocalDateTime.now().toString());

        Response postResponse = given()
                .body(createJson)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post();
//        UUID id = UUID.fromString(getIdHeaderLocation(postResponse));
//
//        postResponse
//                .then()
//                .statusCode(HttpStatus.CREATED.value());

//        validateById(id);
    }

    private void validateById(UUID id) {
        given()
                .pathParam("id", id)
                .when()
                .get("/{id}")
                .then()
                .body("title", is("CREATE TEST TITLE"))
                .body("synopsis", is("CREATE TEST SYNOPSIS"))
                .body("userRating", is(5))
                .statusCode(HttpStatus.OK.value());

    }


}
