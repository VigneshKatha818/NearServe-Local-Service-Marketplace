package com.nearserve.localservices.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String phoneNumber;
    private String password;

}