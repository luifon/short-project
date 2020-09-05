package com.example.demo.domains.movies.repository;

import com.example.demo.domains.movies.dtos.MovieByIdDTO;
import com.example.demo.domains.movies.entity.Movie;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class MovieRepositoryImpl implements MovieRepository {

    private final MovieRepositoryJpa movieRepositoryJpa;

    public MovieRepositoryImpl(MovieRepositoryJpa movieRepositoryJpa) {
        this.movieRepositoryJpa = movieRepositoryJpa;
    }

    @Override
    public Movie findById(UUID id) {
        return movieRepositoryJpa.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MOVIE NOT FOUND."));
    }

    @Override
    public List<MovieByIdDTO> findAll() {
        return movieRepositoryJpa.findAll().stream()
                .map(MovieByIdDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Movie save(Movie movie) {
        return movieRepositoryJpa.save(movie);
    }

}
