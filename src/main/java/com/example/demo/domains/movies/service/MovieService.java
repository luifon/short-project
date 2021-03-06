package com.example.demo.domains.movies.service;

import com.example.demo.domains.movies.dtos.MovieByIdDTO;
import com.example.demo.domains.movies.dtos.MovieCreateDTO;
import com.example.demo.domains.movies.dtos.MovieUpdateDTO;
import com.example.demo.domains.movies.dtos.QuantityLetterDTO;
import com.example.demo.domains.movies.entity.Movie;
import com.example.demo.domains.movies.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public List<MovieByIdDTO> findAll() {
        return movieRepository.findAll();
    }

    public MovieByIdDTO findById(UUID id) {
        return new MovieByIdDTO(movieRepository.findById(id));
    }

    public Movie create(MovieCreateDTO movie) {
        return movieRepository.save(new Movie(movie));
    }

    public Movie update(UUID id, MovieUpdateDTO movie) {
        Movie byId = movieRepository.findById(id);
        byId.update(movie);
        return movieRepository.save(byId);
    }

    public List<QuantityLetterDTO> findByLetterMetricTop10() {
        List<Character> consonants = List.of('B', 'C', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z');
        List<String> allTitles = getAllTitles();

        List<QuantityLetterDTO> collect = consonants.stream().map(letter -> {
            Integer sumOfLetter = allTitles.stream().map(s -> getCharFreq(s, letter)).reduce(0, Integer::sum);

            return new QuantityLetterDTO(letter, sumOfLetter);
        }).sorted(Comparator.comparingInt(QuantityLetterDTO::getQuantity)).collect(Collectors.toList());

        Collections.reverse(collect);
        return collect.subList(0, 10);
    }

    private List<String> getAllTitles() {
        return movieRepository.findAll().stream()
                .map(MovieByIdDTO::getTitle).map(String::toUpperCase).collect(Collectors.toList());
    }

    public int getCharFreq(String s, Character letter) {
        long count = s.codePoints().filter(ch -> ch == letter).count();
        return Math.toIntExact(count);
    }

    public void delete(UUID id) {
        Movie byId = movieRepository.findById(id);
        byId.delete();
        movieRepository.save(byId);

    }

}
