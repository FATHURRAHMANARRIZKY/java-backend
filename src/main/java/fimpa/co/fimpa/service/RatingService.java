package fimpa.co.fimpa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import fimpa.co.fimpa.model.Rating;
import fimpa.co.fimpa.model.Users;
import fimpa.co.fimpa.repository.RatingRepository;
import fimpa.co.fimpa.repository.UsersRepository;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UsersRepository repository;

    public RatingService(RatingRepository ratingRepository, UsersRepository repository) {
        this.repository = repository;
        this.ratingRepository = ratingRepository;
    }

    public Rating getRatingByEmail(String email) {
        return ratingRepository.findByEmail(email);
    }

    public Optional<Users> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public Rating getRatingById(String id) {
        return ratingRepository.findById(id).orElse(null);
    }

    public Rating addRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Rating updateRating(String id, Rating updateRating) {
        return ratingRepository.findById(id).map(existingRating -> {
            existingRating.setName(updateRating.getName());
            existingRating.setEmail(updateRating.getEmail());
            existingRating.setRating(updateRating.getRating());
            existingRating.setComment(updateRating.getComment());
            return ratingRepository.save(existingRating);
        }).orElse(null);
    }

    public boolean deleteRating(String id) {
        if (ratingRepository.existsById(id)) {
            ratingRepository.deleteById(id);
            return true;
        }
        return false;
    }
}