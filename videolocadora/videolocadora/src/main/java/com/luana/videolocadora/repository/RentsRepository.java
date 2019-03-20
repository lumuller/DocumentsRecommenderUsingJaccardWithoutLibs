package com.luana.videolocadora.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luana.videolocadora.models.Movie;
import com.luana.videolocadora.models.Rent;

public interface RentsRepository extends JpaRepository<Rent, Integer>{
	
	Rent findByMovie(Movie movie);
	
	List<Rent> findAllByMovie(Movie movie);
			
}
