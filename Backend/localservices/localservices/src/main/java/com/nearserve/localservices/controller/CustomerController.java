package com.nearserve.localservices.controller;

import com.nearserve.localservices.dto.AddListingRequest;
import com.nearserve.localservices.dto.AddReviewRequest;
import com.nearserve.localservices.entity.*;
import com.nearserve.localservices.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.nearserve.localservices.entity.ServiceType;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final ServiceProviderListingRepository listingRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public CustomerController(ServiceProviderListingRepository listingRepository,
                              UserRepository userRepository,ReviewRepository reviewRepository) {
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    @PostMapping("/add-provider")
    public ResponseEntity<?> addProvider(@RequestBody AddListingRequest request,
                                         Authentication authentication) {

        String phone = authentication.getName();
        User customer = userRepository.findByPhoneNumber(phone).orElseThrow();

        if (listingRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            return ResponseEntity.badRequest().body("Provider already exists");
        }

        ServiceProviderListing listing = new ServiceProviderListing();
        listing.setName(request.getName());
        listing.setPhoneNumber(request.getPhoneNumber());
        listing.setDescription(request.getDescription());
        listing.setStatus(ListingStatus.UNCLAIMED);
        listing.setCreatedBy(customer);
        listing.setLinkedUser(null);
        listing.setServiceType(ServiceType.valueOf(request.getServiceType().toUpperCase()));

        listingRepository.save(listing);

        return ResponseEntity.ok("Provider added and waiting for admin approval");
    }

    @PostMapping("/add-review")
    public ResponseEntity<?> addReview(@RequestBody AddReviewRequest request,
                                       Authentication authentication) {


        String phone = authentication.getName();

        User customer = userRepository.findByPhoneNumber(phone).orElseThrow();

        ServiceProviderListing listing =
                listingRepository.findById(request.getListingId()).orElseThrow();

        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCustomer(customer);
        review.setListing(listing);

        reviewRepository.save(review);

        return ResponseEntity.ok("Review added successfully");


    }

    @GetMapping("/reviews/{listingId}")
    public List<Review> getReviews(@PathVariable Long listingId) {


        ServiceProviderListing listing =
                listingRepository.findById(listingId).orElseThrow();

        return reviewRepository.findByListing(listing);


    }


}