package com.example.movie.expection;

public class MovieNotFoundException  extends RuntimeException{
    public MovieNotFoundException(String message){
        super(message);
    }
}
