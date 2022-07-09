package com.devsuperior.movieflix.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.dto.UserDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;


@Service
public class ReviewService{

    @Autowired
	private ReviewRepository repository;
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
	private AuthService authService;

	@Transactional(readOnly = true)
	public ReviewDTO findById(Long id) {	
		Optional<Review> obj = repository.findById(id);
		Review entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
		return new ReviewDTO(entity);
	}
	
	@Transactional(readOnly = true)
	public List<ReviewDTO> findListOfReviewDTOByMovieId(Long id) {	
		List<Review> list = repository.find(id);
		return list.stream().map(x -> new ReviewDTO(x)).collect(Collectors.toList());
	}
	
	@Transactional
	public ReviewDTO insert(ReviewDTO dto) {
		Review entity = new Review();
		entity.setText(dto.getText());
		
		Movie movie = movieRepository.getOne(dto.getMovieId());
		entity.setMovie(movie);

		User user = authService.authenticated();
		entity.setUser(user);
		UserDTO userDTO = new UserDTO(user);
		
		entity = repository.save(entity);
		return new ReviewDTO(entity, userDTO);
	}
}
