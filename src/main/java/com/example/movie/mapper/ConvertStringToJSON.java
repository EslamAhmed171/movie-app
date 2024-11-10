package com.example.movie.mapper;

import com.example.movie.dto.MovieDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertStringToJSON {
    public static MovieDTO toMovieDTO(String StringMovieDTO) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(StringMovieDTO, MovieDTO.class);
    }
}
