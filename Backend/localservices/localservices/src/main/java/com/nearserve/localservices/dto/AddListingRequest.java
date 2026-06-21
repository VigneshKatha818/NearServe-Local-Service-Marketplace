package com.nearserve.localservices.dto;

import lombok.Data;

@Data
public class AddListingRequest {

    private String name;
    private String phoneNumber;
    private String description;
    private String serviceType;
}
