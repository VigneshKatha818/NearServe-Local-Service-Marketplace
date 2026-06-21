package com.nearserve.localservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NearbyProviderResponse {

    private Long id;
    private String name;
    private String phoneNumber;
    private String description;

    private String locality;
    private String city;

    private double distanceKm;

    private String googleMapsLink;

    private Double averageRating;
    private Long totalReviews;

}