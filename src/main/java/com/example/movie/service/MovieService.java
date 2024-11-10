package com.example.movie.service;

import com.example.movie.dto.MovieDTO;
import com.example.movie.dto.MoviePageResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface MovieService {
    MovieDTO addMovie(MovieDTO movieDTO, MultipartFile file) throws IOException, RuntimeException;
    MovieDTO getMovie(Integer id);
    List<MovieDTO> getAllMovies();
    MovieDTO updateMovie(Integer id, MovieDTO movieDTO, MultipartFile file) throws IOException;
    String deleteMovie(Integer id) throws IOException;
    MoviePageResponse getAllMoviesWithPage(Integer pageNumber, Integer pageSize);
    MoviePageResponse getAllMoviesWithPageWithSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
