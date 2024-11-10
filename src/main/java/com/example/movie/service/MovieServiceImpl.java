package com.example.movie.service;

import com.example.movie.dto.MovieDTO;
import com.example.movie.dto.MoviePageResponse;
import com.example.movie.entity.Movie;
import com.example.movie.expection.FileExistsException;
import com.example.movie.expection.MovieNotFoundException;
import com.example.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final FileService fileService;
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        this.fileService = new FileServiceImpl();

    }

    @Value("${project.poster}")
    private String path;


    @Value("${base.url}")
    private String baseUrl;

    @Override
    public MovieDTO addMovie(MovieDTO movieDTO, MultipartFile file) throws IOException {
        // upload the file
        if (Files.exists(Paths.get(path + File.separator + ';' + file.getOriginalFilename()))) {
            throw new FileExistsException("File already exists: " + file.getOriginalFilename());
        }
        String uploadedFileName = fileService.uploadFile(path, file);

        // set value of poster in the MovieDTO object
        movieDTO.setPoster(uploadedFileName);

        // map DTO to the Movie Object
        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setDirector(movieDTO.getDirector());
        movie.setStudio(movieDTO.getStudio());
        movie.setReleaseYear(movieDTO.getReleaseYear());
        movie.setMovieCast(movieDTO.getMovieCast());
        movie.setPoster(movieDTO.getPoster());

        // save Movie object to the database
        movieRepository.save(movie);

        // generate the posterUrl
        String posterUrl = baseUrl + "/file/" + uploadedFileName;

        // map the Movie object to the DTO object
        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getReleaseYear(),
                movie.getMovieCast(),
                movie.getPoster(),
                posterUrl
        );
    }

    @Override
    public MovieDTO getMovie(Integer id) {
        // check if the data in DB exist, fetch the data of given ID
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("Movie not found!"));

        // generate poster URl
        String posterUrl = baseUrl + "/file/" + movie.getPoster();

        // map Movie object to MovieDTO object
        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getReleaseYear(),
                movie.getMovieCast(),
                movie.getPoster(),
                posterUrl
        );
    }

    @Override
    public List<MovieDTO> getAllMovies() {
        // fetch all data form DB
        List<Movie> movies = movieRepository.findAll();

        // iterate through the list, generate posterURL for each movie object
        // map each movie object to MovieDTO, append it to the list of MovieDTOs
        List<MovieDTO> movieDTOList = new ArrayList<>();
        for(Movie movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            movieDTOList.add( new MovieDTO(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getReleaseYear(),
                    movie.getMovieCast(),
                    movie.getPoster(),
                    posterUrl
            ));
        }

        return movieDTOList;
    }

    @Override
    public MovieDTO updateMovie(Integer id, MovieDTO movieDTO, MultipartFile file) throws IOException {
        // check if the data in DB exist, fetch the data of given ID
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("Movie not found!"));

        // if file is null, do nothing
        // else if file is not null, delete the existing file associated with the record
        // upload the new file
        // update the post URL
        // set value of poster in the MovieDTO object
        String fileName = movie.getPoster();
        if (file != null) {
            Files.deleteIfExists(Paths.get(path + File.separator+ ';' + fileName));
            fileName = fileService.uploadFile(path, file);
        }

        // set value of poster in the MovieDTO object
        movieDTO.setPoster(fileName);

        // map movieDTO object ot Movie object
        movie.setId(id);
        movie.setTitle(movieDTO.getTitle());
        movie.setDirector(movieDTO.getDirector());
        movie.setStudio(movieDTO.getStudio());
        movie.setReleaseYear(movieDTO.getReleaseYear());
        movie.setMovieCast(movieDTO.getMovieCast());
        movie.setPoster(movieDTO.getPoster());

        // save Movie object the DB
        movieRepository.save(movie);

        // generate the posterUrl
        String posterUrl = baseUrl + "/file/" + fileName;

        // map the Movie object to the DTO object
        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getReleaseYear(),
                movie.getMovieCast(),
                movie.getPoster(),
                posterUrl
        );
    }

    @Override
    public String deleteMovie(Integer id) throws IOException {
        // check if the data in DB exist, fetch the data of given ID
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("Movie not found!"));

        // delete the file associated with this object
        Files.deleteIfExists(Paths.get(path + File.separator + movie.getPoster()));

        // delete the movie object also
        movieRepository.delete(movie);

        return "Movie deleted!";
    }

    @Override
    public MoviePageResponse getAllMoviesWithPage(Integer pageNumber, Integer pageSize) {
        // create Pageable object with page number and page size, find pages of movies
        Pageable pageable =  PageRequest.of(pageNumber, pageSize);
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movieList = moviePages.getContent();

        // iterate through the list, generate posterURL for each movie object
        // map each movie object to MovieDTO, append it to the list of MovieDTOs
        List<MovieDTO> movieDTOList = new ArrayList<>();
        for(Movie movie : movieList) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            movieDTOList.add( new MovieDTO(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getReleaseYear(),
                    movie.getMovieCast(),
                    movie.getPoster(),
                    posterUrl
            ));
        }

        // creat the movie page response and return it
        return new MoviePageResponse(
                movieDTOList,
                pageNumber,
                pageSize,
                moviePages.getNumberOfElements(),
                moviePages.getTotalPages(),
                moviePages.isLast()
        );
    }

    @Override
    public MoviePageResponse getAllMoviesWithPageWithSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equals("asc")? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable =  PageRequest.of(pageNumber, pageSize, sort);
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movieList = moviePages.getContent();

        // iterate through the list, generate posterURL for each movie object
        // map each movie object to MovieDTO, append it to the list of MovieDTOs
        List<MovieDTO> movieDTOList = new ArrayList<>();
        for(Movie movie : movieList) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            movieDTOList.add( new MovieDTO(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getReleaseYear(),
                    movie.getMovieCast(),
                    movie.getPoster(),
                    posterUrl
            ));
        }

        // creat the movie page response and return it
        return new MoviePageResponse(
                movieDTOList,
                pageNumber,
                pageSize,
                moviePages.getNumberOfElements(),
                moviePages.getTotalPages(),
                moviePages.isLast()
        );
    }


}
