package fimpa.co.fimpa.service;

import java.util.List;
import org.springframework.stereotype.Service;
import fimpa.co.fimpa.model.Rating;
import fimpa.co.fimpa.repository.RatingRepository;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository){
        this.ratingRepository = ratingRepository;
    }

    public List<Rating> getAllRatings(){
        return ratingRepository.findAll();
    }

    public Rating getRatingById(String id){
        return ratingRepository.findById(id).orElse(null);
    }

    public Rating addRating(Rating rating){
        return ratingRepository.save(rating);
    }

    public Rating updateRating(String id, Rating updateRating){
        return ratingRepository.findById(id).map(existingRating -> {
            existingRating.setName(updateRating.getName());
            existingRating.setRating(updateRating.getRating());
            existingRating.setComment(updateRating.getComment());
            return ratingRepository.save(existingRating);
        }).orElse(null);
    }

    public boolean deleteRating(String id){
        if(ratingRepository.existsById(id)) {
            ratingRepository.deleteById(id);
            return true;
        }
        return false;
    }
}