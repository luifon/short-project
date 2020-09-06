package com.example.demo.domains.movies.controller;

import com.example.demo.domains.movies.dtos.MovieByIdDTO;
import com.example.demo.domains.movies.dtos.MovieCreateDTO;
import com.example.demo.domains.movies.dtos.MovieUpdateDTO;
import com.example.demo.domains.movies.dtos.QuantityLetterDTO;
import com.example.demo.domains.movies.entity.Movie;
import com.example.demo.domains.movies.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("movies")
public class MovieController {

    @Autowired
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieByIdDTO> findAll() {
        return movieService.findAll();
    }

    @GetMapping("/{id}")
    public Movie findById(@PathVariable("id") UUID id) {
        return movieService.findById(id);
    }

    @GetMapping("/letter_metrics_top10")
    public List<QuantityLetterDTO> findByLetterMetricTop10() {
        return movieService.findByLetterMetricTop10();
    }

    @PostMapping()
    public Movie save(@RequestBody MovieCreateDTO movie) {
        return movieService.create(movie);
    }

    @PutMapping("/{id}")
    public Movie update(@PathVariable("id") UUID id, @RequestBody MovieUpdateDTO movie) {
        return movieService.update(id, movie);
    }

}
