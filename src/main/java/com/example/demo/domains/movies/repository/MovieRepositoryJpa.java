package com.example.demo.domains.movies.repository;

import com.example.demo.domains.movies.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MovieRepositoryJpa extends JpaRepository<Movie, UUID> {

}
