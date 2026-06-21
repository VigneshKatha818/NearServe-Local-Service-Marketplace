package com.nearserve.localservices.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String locality;

    private String city;

    private String state;

    private String pincode;

    private Double latitude;

    private Double longitude;

    @OneToOne
    @JoinColumn(name = "listing_id")
    private ServiceProviderListing listing;
}