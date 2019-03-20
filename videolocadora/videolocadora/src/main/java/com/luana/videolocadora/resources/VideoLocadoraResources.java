package com.luana.videolocadora.resources;


import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luana.videolocadora.models.Movie;
import com.luana.videolocadora.models.Rent;
import com.luana.videolocadora.models.User;
import com.luana.videolocadora.repository.MoviesRepository;
import com.luana.videolocadora.repository.RentsRepository;
import com.luana.videolocadora.repository.UsersRepository;

@RestController
@RequestMapping(value="/www.videolocadora.com")
public class VideoLocadoraResources {
	
	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	MoviesRepository moviesRepository;
	
	@Autowired
	RentsRepository rentsRepository;
	
	//TESTAR
	/*@GetMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response){	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);            
        }
	}*/
		
	//Métodos para CRUD de Filmes
	@PostMapping("/movies")
	public Movie saveMovies(@RequestBody Movie movie) {
		return moviesRepository.save(movie);
	}	
	
	//DEVEM SER FILMES DISPONIVEIS
	@GetMapping("/movies")
	public List<Movie> listMovies(){	
		return moviesRepository.findAll().stream().filter(f -> f.isAvailable() == true).collect(Collectors.toList()); 
	}
	
	@DeleteMapping("/movies/{movieId}")
	public void deleteFilme(@PathVariable(value="movieId") Integer movieId) throws SQLIntegrityConstraintViolationException  {
		moviesRepository.deleteById(movieId);
	}
	
	@PutMapping("/movies")
	public Movie updateMovie(@RequestBody Movie movie) {
		return moviesRepository.save(movie);
	}
	
	//Métodos para CRUD de Usuarios
	@PostMapping("/users")
	public User saveUser(@RequestBody User user) {		
		return usersRepository.save(user);
	}
	
	@GetMapping("/users")
	public List<User> listUsers(){		
		return usersRepository.findAll();
	}
	
	@DeleteMapping("/users/{username}")
	public void deleteUser(@PathVariable(value="username") String username) throws SQLIntegrityConstraintViolationException {
		usersRepository.deleteById(username);		
	}
	
	@PutMapping("/users")
	public User updateUser(@RequestBody User user) {
		return usersRepository.save(user);
	}	
	
	@PostMapping("/rents/{movieId}")
	public String rentMovie(@PathVariable(value="movieId") Integer movieId) {						
		Rent newRentRegister = new Rent();		
		Optional<Movie> requiredMovie = moviesRepository.findById(movieId);
		if(requiredMovie.isPresent()) {
			if(requiredMovie.get().isAvailable()) {
				requiredMovie.get().setAvailable(false);
				newRentRegister.setMovie(requiredMovie.get());
				
				User user = usersRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
				newRentRegister.setUser(user);
				
				newRentRegister.setRentalDate(new Date());
			
				rentsRepository.save(newRentRegister);
				
				return String.format("Locação do filme %s (id %d) efetuada com sucesso por %s.", requiredMovie.get().getTitle(), requiredMovie.get().getId(),user.getName());
			} else {
				
				return String.format("O filme %s (id %d) não está disponível para locação.", requiredMovie.get().getTitle(), requiredMovie.get().getId());
			}
		} else {
			return String.format("Não foi possível identificar um filme com o id %d.", movieId);
		}
	}	
	
		
	//TESTAR
	@PutMapping("/rents/{movieId}")
	public String returnMovie(@PathVariable(value="movieId") Integer movieId) {							
				
		Optional<Movie> requiredMovie = moviesRepository.findById(movieId);
		if(requiredMovie.isPresent()) {
			
			User user = usersRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			
			List<Rent> movieRents = rentsRepository.findAllByMovie(requiredMovie.get());
			
			List<Rent> movieRentsPerformedByCurrentUser = movieRents.stream()  
		                .filter(str -> str.getUser().getUsername().equals(user.getUsername()) && str.getReturnDate() == null) 
		                .collect(Collectors.toList());  
			
			if(movieRentsPerformedByCurrentUser.isEmpty()) {
				return String.format("Não foi possível identificar um registro de locação do filme %s em aberto para seu usuário (%s). ", movieId, user.getUsername());
			} else {
				Rent rentalRegister = movieRentsPerformedByCurrentUser.iterator().next();
				
				rentalRegister.setReturnDate(new Date());
				rentalRegister.getMovie().setAvailable(true);				
				
				moviesRepository.save(rentalRegister.getMovie());
				rentsRepository.save(rentalRegister);
				
				return String.format("%s, o filme %s (id %d) foi devolvido com sucesso.", user.getName(), requiredMovie.get().getTitle(), requiredMovie.get().getId());
			}			
		} else {
			return String.format("Não foi possível identificar um filme com o id %d.", movieId);
		}
	}
	
	@GetMapping("/rents")
	public List<RentsSimplified> listNotReturnedRents(){	
		List<Rent> rentsNotReturned = rentsRepository.findAll().stream().filter(l -> l.getReturnDate() == null).collect(Collectors.toList()); 
		List<RentsSimplified> presentationList = new ArrayList<>();
		for(Rent l : rentsNotReturned){
			presentationList.add(new RentsSimplified(l.getMovie().getId(), l.getMovie().getTitle(), l.getUser().getUsername(), 
					l.getRentalDate(), l.getReturnDate()));
		}
		return presentationList;
	}
	
	@GetMapping("/rents/{movieId}")
	public List<RentsSimplified> listRentsFromMovie(@PathVariable(value="movieId") Integer movieId){	
		
		Optional<Movie> requiredMovie = moviesRepository.findById(movieId);
		if(requiredMovie.isPresent()) {		
			List<Rent> rentsFromMovie = rentsRepository.findAllByMovie(requiredMovie.get());
			
			List<RentsSimplified> presentationList = new ArrayList<>();
			for(Rent l : rentsFromMovie){
				presentationList.add(new RentsSimplified(l.getMovie().getId(), l.getMovie().getTitle(), l.getUser().getUsername(), 
						l.getRentalDate(), l.getReturnDate()));
			}
			
			presentationList.sort((RentsSimplified l1, RentsSimplified l2)->l2.getRentalDate().compareTo(l1.getRentalDate()));
										
			return presentationList;	
		}
		
		return null;
	}
	
	private class RentsSimplified {
		private Integer idMovie;
		private String movieTitle;
		private String username;
		private Date rentalDate;
		private Date returnDate;
		
		public RentsSimplified(Integer idMovie, String movieTitle, String username, Date rentalDate,
				Date returnDate) {
			super();
			this.idMovie = idMovie;
			this.movieTitle = movieTitle;
			this.username = username;
			this.rentalDate = rentalDate;
			this.returnDate = returnDate;
		}

		public Integer getIdMovie() {
			return idMovie;
		}

		public String getMovieTitle() {
			return movieTitle;
		}

		public String getUsername() {
			return username;
		}

		public Date getRentalDate() {
			return rentalDate;
		}

		public Date getReturnDate() {
			return returnDate;
		}		
	}	
}
