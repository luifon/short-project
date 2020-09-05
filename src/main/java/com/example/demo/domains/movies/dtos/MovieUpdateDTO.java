package com.example.demo.domains.movies.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MovieUpdateDTO {

    @Min(0)
    @Max(10)
    private int userRating;

}
