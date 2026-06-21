package com.nearserve.localservices.controller;

import com.nearserve.localservices.dto.NearbyProviderResponse;
import com.nearserve.localservices.dto.PublicListingResponse;
import com.nearserve.localservices.entity.*;
import com.nearserve.localservices.repository.ReviewRepository;
import com.nearserve.localservices.repository.ServiceProviderListingRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final ServiceProviderListingRepository listingRepository;
    private final ReviewRepository reviewRepository;

    public PublicController(ServiceProviderListingRepository listingRepository,
                            ReviewRepository reviewRepository) {
        this.listingRepository = listingRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/providers")
    public List<PublicListingResponse> getVerifiedProviders() {

        List<ServiceProviderListing> listings =
                listingRepository.findByStatus(ListingStatus.VERIFIED);

        return listings.stream()
                .map(listing -> new PublicListingResponse(
                        listing.getId(),
                        listing.getName(),
                        listing.getPhoneNumber(),
                        listing.getDescription()
                ))
                .toList();
    }

    @GetMapping("/providers/by-city")
    public List<PublicListingResponse> getProvidersByCity(@RequestParam String city) {

        List<ServiceProviderListing> listings =
                listingRepository.findVerifiedByCity(city);

        return listings.stream()
                .map(listing -> new PublicListingResponse(
                        listing.getId(),
                        listing.getName(),
                        listing.getPhoneNumber(),
                        listing.getDescription()
                ))
                .toList();
    }

    @GetMapping("/providers/search")
    public List<PublicListingResponse> searchProviders(
            @RequestParam String locality,
            @RequestParam String service) {

        ServiceType serviceType =
                ServiceType.valueOf(service.toUpperCase());

        List<ServiceProviderListing> listings =
                listingRepository.searchByLocalityAndService(locality, serviceType);

        return listings.stream()
                .map(listing -> new PublicListingResponse(
                        listing.getId(),
                        listing.getName(),
                        listing.getPhoneNumber(),
                        listing.getDescription()
                ))
                .toList();
    }

    @GetMapping("/providers/nearby")
    public List<NearbyProviderResponse> findNearbyProviders(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam double radius,
            @RequestParam String service) {

        ServiceType serviceType =
                ServiceType.valueOf(service.toUpperCase());

        List<Object[]> results =
                listingRepository.findNearbyProvidersWithDistance(
                        lat, lng, radius, serviceType);

        return results.stream()
                .map(obj -> {
                    ServiceProviderListing listing = (ServiceProviderListing) obj[0];
                    Double distance = (Double) obj[1];


                    Address address = listing.getAddress();

                    Double avgRating = reviewRepository
                            .findAverageRatingByListingId(listing.getId());

                    Long totalReviews = reviewRepository
                            .countReviewsByListingId(listing.getId());

                    if (avgRating == null) avgRating = 0.0;

                    return new NearbyProviderResponse(
                            listing.getId(),
                            listing.getName(),
                            listing.getPhoneNumber(),
                            listing.getDescription(),
                            address.getLocality(),
                            address.getCity(),
                            Math.round(distance * 100.0) / 100.0,
                            "https://www.google.com/maps?q=" +
                                    address.getLatitude() + "," + address.getLongitude(),
                            Math.round(avgRating * 10.0) / 10.0,
                            totalReviews
                    );
                })
                .toList();


    }
}