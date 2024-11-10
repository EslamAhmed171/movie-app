package com.example.movie.expection;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus code, String message) {
}
