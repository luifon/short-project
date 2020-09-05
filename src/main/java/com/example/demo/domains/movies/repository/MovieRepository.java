package com.example.demo.domains.movies.repository;

import com.example.demo.domains.movies.dtos.MovieByIdDTO;
import com.example.demo.domains.movies.entity.Movie;

import java.util.List;
import java.util.UUID;

public interface MovieRepository {

    Movie findById(UUID id);

    List<MovieByIdDTO> findAll();

    Movie save(Movie movie);
}
