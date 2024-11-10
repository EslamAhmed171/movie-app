package com.example.movie.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Please provide movie's title")
    private String title;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Please provide movie's director")
    private String director;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Please provide movie's studio")
    private String studio;


    @Column(nullable = false)
    private int releaseYear;

    @ElementCollection
    @CollectionTable(name = "movie_cast")
    @Column(name = "cast_member")
    private Set<String> movieCast;

    @Column(nullable = false)
    @NotBlank(message = "Please provide movie's poster")
    private String poster;
}
