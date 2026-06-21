package com.nearserve.localservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminListingResponse {

    private Long id;
    private String name;
    private String phoneNumber;
    private String description;
    private String status;
    private String createdByName;

    // Address details
    private String locality;
    private String city;
    private String state;
    private String pincode;

    // KYC details
    private String aadharNumber;
    private String panNumber;
}