package com.luana.videolocadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luana.videolocadora.models.Movie;

public interface MoviesRepository extends JpaRepository<Movie, Integer> {

	Movie findById(String id);

}
