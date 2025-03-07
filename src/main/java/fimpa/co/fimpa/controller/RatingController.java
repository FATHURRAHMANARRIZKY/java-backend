package fimpa.co.fimpa.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fimpa.co.fimpa.model.Rating;
import fimpa.co.fimpa.model.Users;
import fimpa.co.fimpa.service.RatingService;
import fimpa.co.fimpa.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingService;

    private final UsersService service;

    public RatingController(RatingService ratingService, UsersService service) {
        this.service = service;
        this.ratingService = ratingService;
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<Rating> getUserRating(@PathVariable String email) {
        try {
            Rating rating = ratingService.getRatingByEmail(email);
            return rating != null ? ResponseEntity.ok(rating) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings() {
        try {
            List<Rating> ratings = ratingService.getAllRatings();
            return ResponseEntity.ok(ratings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rating> getRatingById(@PathVariable String id) {
        try {
            Rating rating = ratingService.getRatingById(id);
            return rating != null ? ResponseEntity.ok(rating) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Rating> addRating(@RequestBody Rating rating, HttpServletRequest request) {
        try {
            // Get email from the request attribute set in JwtAuthenticationFilter
            String email = (String) request.getAttribute("email");
            System.out.println(email);

            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            // Get the user by email
            Users user = service.findByEmail(email);

            if (user != null) {
                rating.setEmail(email); // Set email in the rating
                rating.setName(user.getUsername()); // Set user's name in the rating

                Rating createdRating = ratingService.addRating(rating); // Save the rating
                return ResponseEntity.status(HttpStatus.CREATED).body(createdRating);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // User not found
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rating> updateRating(@PathVariable String id, @RequestBody Rating updateRating) {
        try {
            Rating rating = ratingService.updateRating(id, updateRating);
            return rating != null ? ResponseEntity.ok(rating) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable String id) {
        try {
            boolean deleted = ratingService.deleteRating(id);
            return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}