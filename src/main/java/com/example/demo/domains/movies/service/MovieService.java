package com.example.demo.domains.movies.service;

import com.example.demo.domains.movies.dtos.MovieByIdDTO;
import com.example.demo.domains.movies.dtos.MovieCreateDTO;
import com.example.demo.domains.movies.dtos.MovieUpdateDTO;
import com.example.demo.domains.movies.entity.Movie;
import com.example.demo.domains.movies.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<MovieByIdDTO> findAll() {
        return movieRepository.findAll();
    }

    public Movie findById(UUID id) {
        return movieRepository.findById(id);
    }

    public Movie create(MovieCreateDTO movie) {
        return movieRepository.save(new Movie(movie));
    }

    public Movie update(UUID id, MovieUpdateDTO movie) {
        Movie byId = movieRepository.findById(id);
        byId.update(movie);
        return movieRepository.save(byId);
    }

}
