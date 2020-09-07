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
    private String movieUpdate;

    @Before
    public void setup() {
        super.setUp();
        RestAssured.basePath = "/movies";
        movieCreate = ResourceUtils.getContentFromResource("/json/movie-create.json");
        movieUpdate = ResourceUtils.getContentFromResource("/json/movie-update.json");
        prepareData();
    }

    private void prepareData() {
        MovieCreateDTO movieCreateDTO = new MovieCreateDTO("NEW MOVIE 1", "THIS IS A MOVIE ABOUT AN INTEGRATION TEST", 5, LocalDateTime.now());
        movieRepository.save(new Movie(movieCreateDTO));
        MovieCreateDTO movieCreateDTO2 = new MovieCreateDTO("NEW MOVIE 2", "THIS IS YET ANOTHER MOVIE ABOUT AN INTEGRATION TEST", 10, LocalDateTime.now());
        movieById = movieRepository.save(new Movie(movieCreateDTO2)).getId();
    }

    @Test
    public void findAll() {
        given()
                .get()
                .then()
                .body("size()", is(2))
                .body("[0].title", is("NEW MOVIE 1"))
                .body("[0].userRating", is(5))
                .body("[1].title", is("NEW MOVIE 2"))
                .body("[1].userRating", is(10));
    }

    @Test
    public void findByLetterMetricTop10() {
        given()
                .get("/letter_metrics_top10")
                .then()
                .body("size()", is(10))
                .body("[0].letter", is("W"))
                .body("[0].quantity", is(2))
                .body("[1].letter", is("V"))
                .body("[1].quantity", is(2))
                .body("[2].letter", is("N"))
                .body("[2].quantity", is(2))
                .body("[3].letter", is("M"))
                .body("[3].quantity", is(2))
                .body("[4].letter", is("Z"))
                .body("[4].quantity", is(0))
                .body("[5].letter", is("Y"))
                .body("[5].quantity", is(0))
                .body("[6].letter", is("X"))
                .body("[6].quantity", is(0))
                .body("[7].letter", is("T"))
                .body("[7].quantity", is(0))
                .body("[8].letter", is("S"))
                .body("[8].quantity", is(0))
                .body("[9].letter", is("R"))
                .body("[9].quantity", is(0));
    }

    @Test
    public void findById() {
        given()
                .pathParam("id", movieById)
                .get("/{id}").then()
                .body("title", is("NEW MOVIE 2"))
                .body("synopsis", is("THIS IS YET ANOTHER MOVIE ABOUT AN INTEGRATION TEST"))
                .body("userRating", is(10));
    }

    @Test
    public void delete() {
        given()
                .pathParam("id", movieById)
                .contentType(ContentType.JSON)
                .when()
                .delete("/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void findById_notFound() {
        given()
                .pathParam("id", UUID.randomUUID())
                .get("/{id}").then()
                .body("message", is("MOVIE NOT FOUND."));
    }

    @Test
    public void create() {
        String createJson = movieCreate
                .replace("TITLE", "CREATE TEST TITLE")
                .replace("SYNOPSIS", "CREATE TEST SYNOPSIS")
                .replace("RELEASE_DATE", LocalDateTime.now().toString());

        Response postResponse = given()
                .body(createJson)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post();
        UUID id = UUID.fromString(getIdHeaderLocation(postResponse));

        postResponse
                .then()
                .statusCode(HttpStatus.CREATED.value());

        validateById(id);
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

    @Test
    public void create_titleTooBig() {
        String createJson = movieCreate
                .replace("TITLE", "CREATE TEST TITLE THAT WILL FAIL BECAUSE THE TITLE IS TOO LONG")
                .replace("SYNOPSIS", "CREATE TEST SYNOPSIS")
                .replace("RELEASE_DATE", LocalDateTime.now().toString());

        given()
                .body(createJson)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .body("message", is("FIELD TITLE SHOULD BE BETWEEN 0 AND 30."))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void create_titleBlank() {
        String createJson = movieCreate
                .replace("TITLE", " ")
                .replace("SYNOPSIS", "CREATE TEST SYNOPSIS")
                .replace("RELEASE_DATE", LocalDateTime.now().toString());

        given()
                .body(createJson)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .body("message", is("FIELD TITLE IS REQUIRED."))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    public void create_ratingInvalidHigher() {
        String createJson = movieCreate
                .replace("TITLE", "INVALID RATING TEST")
                .replace("SYNOPSIS", "CREATE TEST SYNOPSIS")
                .replace("5", "15")
                .replace("RELEASE_DATE", LocalDateTime.now().toString());

        given()
                .body(createJson)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .body("message", is("FIELD USERRATING SHOULD BE EQUAL OR LESS THAN 10."))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void create_ratingInvalidSmaller() {
        String createJson = movieCreate
                .replace("TITLE", "INVALID RATING TEST")
                .replace("SYNOPSIS", "CREATE TEST SYNOPSIS")
                .replace("5", "-5")
                .replace("RELEASE_DATE", LocalDateTime.now().toString());

        given()
                .body(createJson)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .body("message", is("FIELD USERRATING SHOULD BE EQUAL OR GREATER THAN 0."))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void update() {
        given()
                .pathParam("id", movieById)
                .body(movieUpdate)
                .contentType(ContentType.JSON)
                .when()
                .put("/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        validateUpdateById(movieById);
    }

    private void validateUpdateById(UUID id) {
        given()
                .pathParam("id", id)
                .when()
                .get("/{id}")
                .then()
                .body("title", is("NEW MOVIE 2"))
                .body("synopsis", is("THIS IS YET ANOTHER MOVIE ABOUT AN INTEGRATION TEST"))
                .body("userRating", is(8))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void update_invalidRatingHigher() {
        String movieUpdate = this.movieUpdate.replace("8", "11");
        given()
                .pathParam("id", movieById)
                .body(movieUpdate)
                .contentType(ContentType.JSON)
                .when()
                .put("/{id}")
                .then()
                .body("message", is("FIELD USERRATING SHOULD BE EQUAL OR LESS THAN 10."))
                .statusCode(HttpStatus.BAD_REQUEST.value());;
    }



    @Test
    public void update_invalidRatingSmaller() {
        String movieUpdate = this.movieUpdate.replace("8", "-1");
        given()
                .pathParam("id", movieById)
                .body(movieUpdate)
                .contentType(ContentType.JSON)
                .when()
                .put("/{id}")
                .then()
                .body("message", is("FIELD USERRATING SHOULD BE EQUAL OR GREATER THAN 0."))
                .statusCode(HttpStatus.BAD_REQUEST.value());;
    }

}
