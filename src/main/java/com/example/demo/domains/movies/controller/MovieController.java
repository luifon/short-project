package com.example.demo.domains.movies.controller;

import com.example.demo.domains.movies.dtos.MovieByIdDTO;
import com.example.demo.domains.movies.dtos.MovieCreateDTO;
import com.example.demo.domains.movies.dtos.MovieUpdateDTO;
import com.example.demo.domains.movies.dtos.QuantityLetterDTO;
import com.example.demo.domains.movies.entity.Movie;
import com.example.demo.domains.movies.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("movies")
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public List<MovieByIdDTO> findAll() {
        return movieService.findAll();
    }

    @GetMapping("/{id}")
    public MovieByIdDTO findById(@PathVariable("id") UUID id) {
        return movieService.findById(id);
    }

    @GetMapping("/letter_metrics_top10")
    public List<QuantityLetterDTO> findByLetterMetricTop10() {
        return movieService.findByLetterMetricTop10();
    }

    @PostMapping
    public ResponseEntity<Movie> save(@Valid @RequestBody MovieCreateDTO movie) {
        Movie createdMovie = movieService.create(movie);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdMovie.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Movie update(@PathVariable("id") UUID id, @Valid @RequestBody MovieUpdateDTO movie) {
        return movieService.update(id, movie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        movieService.delete(id);
    }

}
