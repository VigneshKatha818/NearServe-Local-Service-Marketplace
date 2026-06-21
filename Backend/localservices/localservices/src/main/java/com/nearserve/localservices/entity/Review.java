package com.nearserve.localservices.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating;

    private String comment;

    @ManyToOne
    private User customer;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private ServiceProviderListing listing;


}
