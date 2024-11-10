package com.example.movie.expection;

public class FileExistsException extends RuntimeException {
    public FileExistsException(String message) {
        super(message);
    }
}

