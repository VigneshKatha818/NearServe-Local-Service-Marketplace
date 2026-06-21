package com.nearserve.localservices.dto;

import com.nearserve.localservices.entity.ServiceType;
import lombok.Data;

@Data
public class RegisterRequest {

    private String name;
    private String phoneNumber;
    private String password;
    private ServiceType serviceType;
}
