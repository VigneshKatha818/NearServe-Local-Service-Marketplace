package com.nearserve.localservices.dto;

import lombok.Data;

@Data
public class AddReviewRequest {


    private Long listingId;
    private int rating;
    private String comment;


}
