package com.example.demo.domains.movies.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MovieCreateDTO {

    @NotBlank
    @Size(max = 30)
    private String title;
    private String synopsis;

    @Min(0)
    @Max(10)
    private int userRating;
    private LocalDateTime releaseDate;

}
