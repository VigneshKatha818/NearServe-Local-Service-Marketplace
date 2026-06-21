package com.nearserve.localservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublicListingResponse {

    private Long id;
    private String name;
    private String phoneNumber;
    private String description;
}