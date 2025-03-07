package fimpa.co.fimpa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import fimpa.co.fimpa.model.Rating;

public interface RatingRepository extends MongoRepository<Rating, String> {
    Rating findByEmail(String email);
}
