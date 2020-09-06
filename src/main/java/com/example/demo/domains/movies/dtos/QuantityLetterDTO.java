package com.example.demo.domains.movies.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuantityLetterDTO {

    private final Character letter;
    private final int quantity;

}
