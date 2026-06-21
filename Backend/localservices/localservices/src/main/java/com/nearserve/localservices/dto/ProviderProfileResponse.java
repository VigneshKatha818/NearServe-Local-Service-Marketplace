package com.nearserve.localservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProviderProfileResponse {

    private String name;
    private String serviceType;
    private String status;

    private String locality;
    private String city;
}