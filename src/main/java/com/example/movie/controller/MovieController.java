package com.example.movie.controller;

import com.example.movie.dto.MovieDTO;
import com.example.movie.dto.MoviePageResponse;
import com.example.movie.expection.EmptyFileException;
import com.example.movie.expection.FileExistsException;
import com.example.movie.mapper.ConvertStringToJSON;
import com.example.movie.service.MovieService;
import com.example.movie.utilis.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add-movie")
    public ResponseEntity<MovieDTO> addMovieHandler(
            @RequestPart MultipartFile file,
            @RequestPart String StringMovieDTO) throws IOException {
        System.out.println(file);
        if (file.isEmpty()){
            throw new EmptyFileException("File is empty!");
        }
        MovieDTO movieDTO = ConvertStringToJSON.toMovieDTO(StringMovieDTO);
        return new ResponseEntity<>(movieService.addMovie(movieDTO, file), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieHandler(@PathVariable Integer id) {
        return new ResponseEntity<>(movieService.getMovie(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAllMoviesHandler() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @GetMapping("/allWithPage")
    public ResponseEntity<MoviePageResponse> getAllWithPageHandler(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize)
    {
        return new ResponseEntity<>(movieService.getAllMoviesWithPage(pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("/allWithPageAndSort")
    public ResponseEntity<MoviePageResponse> getAllWithPageAndSortHandler(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_ASC, required = false) String sortOrder)
    {
        return new ResponseEntity<>(
                movieService.getAllMoviesWithPageWithSorting(
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortOrder),
                HttpStatus.OK);
    }

    @PutMapping("/update-movie/{id}")
    public ResponseEntity<MovieDTO> updateMovieHandler(
            @PathVariable int id,
            @RequestPart String StringMovieDTO,
            @RequestPart MultipartFile file) throws IOException {

        try {
            MovieDTO movieDTO = ConvertStringToJSON.toMovieDTO(StringMovieDTO);
            return new ResponseEntity<>(movieService.updateMovie(id, movieDTO, file), HttpStatus.OK);
        } catch (EmptyFileException ex){
            throw new EmptyFileException("File is empty!");
        } catch (FileExistsException ex){
            throw new FileExistsException("File already exists: " + file.getOriginalFilename());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovieHandler(@PathVariable Integer id) throws IOException {
        return new ResponseEntity<>(movieService.deleteMovie(id), HttpStatus.OK);
    }
}
