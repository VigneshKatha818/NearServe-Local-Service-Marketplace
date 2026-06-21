package com.nearserve.localservices.repository;

import com.nearserve.localservices.entity.Review;
import com.nearserve.localservices.entity.ServiceProviderListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {


    List<Review> findByListing(ServiceProviderListing listing);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.listing.id = :listingId")
    Double findAverageRatingByListingId(@Param("listingId") Long listingId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.listing.id = :listingId")
    Long countReviewsByListingId(@Param("listingId") Long listingId);



}
