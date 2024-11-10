package com.example.movie.dto;


import java.util.List;

public record MoviePageResponse(
        List<MovieDTO> movieDTOs,
        Integer pageNumber,
        Integer pageSize,
        Integer totalElements,
        Integer totalPages,
        boolean isLast
        ) {

}
