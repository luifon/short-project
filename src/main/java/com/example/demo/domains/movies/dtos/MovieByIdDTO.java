package com.example.demo.domains.movies.dtos;

import com.example.demo.domains.movies.entity.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class MovieByIdDTO {

    private UUID id;
    private String title;
    private String synopsis;
    private int userRating;
    private LocalDateTime releaseDate;

    public MovieByIdDTO(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.synopsis = movie.getSynopsis();
        this.userRating = movie.getUserRating();
        this.releaseDate = movie.getReleaseDate();
    }

    public java.util.UUID getId() {
        return this.id;
    }

    public java.lang.String getTitle() {
        return this.title;
    }

    public java.lang.String getSynopsis() {
        return this.synopsis;
    }

    public int getUserRating() {
        return this.userRating;
    }

    public java.time.LocalDateTime getReleaseDate() {
        return this.releaseDate;
    }

}
