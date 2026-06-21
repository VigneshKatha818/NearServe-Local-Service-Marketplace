package com.nearserve.localservices.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    private String name;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    private String description;

    @Enumerated(EnumType.STRING)
    private ListingStatus status;

    @OneToOne(mappedBy = "listing", cascade = CascadeType.ALL)
    private Address address;

    // Who added this listing (customer)
    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    // Linked service provider account (if claimed)
    @OneToOne
    @JoinColumn(name = "linked_user_id")
    private User linkedUser;
}
