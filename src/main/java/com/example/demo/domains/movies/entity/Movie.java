package com.example.demo.domains.movies.entity;

import com.example.demo.domains.movies.dtos.MovieCreateDTO;
import com.example.demo.domains.movies.dtos.MovieUpdateDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
@Where(clause = "deleted_at is null")
@Table(name = "tb_movie", schema = "newproj")
public class Movie {

    @Id
    private UUID id;
    private String title;
    private String synopsis;
    private int userRating;
    private LocalDateTime releaseDate;
    private LocalDateTime deletedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Movie(MovieCreateDTO movie) {
        this.id = UUID.randomUUID();
        this.title = movie.getTitle();
        this.synopsis = movie.getSynopsis();
        this.userRating = movie.getUserRating();
        this.releaseDate = movie.getReleaseDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void update(MovieUpdateDTO movie) {
        this.userRating = movie.getUserRating();
    }

    public java.util.UUID getId(){return this.id;}

    public java.lang.String getTitle(){return this.title;}

    public java.lang.String getSynopsis(){return this.synopsis;}

    public int getUserRating(){return this.userRating;}

    public java.time.LocalDateTime getReleaseDate(){return this.releaseDate;}

    public java.time.LocalDateTime getDeletedAt(){return this.deletedAt;}

    public java.time.LocalDateTime getCreatedAt(){return this.createdAt;}

    public java.time.LocalDateTime getUpdatedAt(){return this.updatedAt;}

    public Movie(){}

}
