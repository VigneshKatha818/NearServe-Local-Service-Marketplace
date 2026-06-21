package com.nearserve.localservices.dto;

import lombok.Data;

@Data
public class UpdateAddressRequest {

    private String locality;
    private String city;
    private String state;
    private String pincode;
    private Double latitude;
    private Double longitude;
}