package com.nearserve.localservices.controller;

import com.nearserve.localservices.dto.AddressRequest;
import org.springframework.web.bind.annotation.*;
import com.nearserve.localservices.dto.UpdateAddressRequest;
import com.nearserve.localservices.entity.*;
import com.nearserve.localservices.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import com.nearserve.localservices.entity.ProviderVerification;
import com.nearserve.localservices.repository.ProviderVerificationRepository;
import com.nearserve.localservices.dto.KycRequest;
import com.nearserve.localservices.dto.ProviderProfileResponse;


@RestController
@RequestMapping("/provider")
public class ProviderController {

    private final UserRepository userRepository;
    private final ServiceProviderListingRepository listingRepository;
    private final AddressRepository addressRepository;
    private final ProviderVerificationRepository verificationRepository;

    public ProviderController(UserRepository userRepository,
                              ServiceProviderListingRepository listingRepository,
                              AddressRepository addressRepository,
                              ProviderVerificationRepository verificationRepository) {

        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
        this.addressRepository = addressRepository;
        this.verificationRepository = verificationRepository;
    }

    @GetMapping("/test")
    public String providerTest() {
        return "Provider endpoint accessed successfully";
    }

    @PostMapping("/update-address")
    public ResponseEntity<String> updateAddress(
            @RequestBody AddressRequest request,
            Authentication authentication) {


// ✅ Validate required fields
        if (request.getLocality() == null ||
                request.getCity() == null ||
                request.getState() == null ||
                request.getPincode() == null) {

            return ResponseEntity.badRequest()
                    .body("All address fields are required");
        }

// ✅ CRITICAL: Validate latitude & longitude
        if (request.getLatitude() == null || request.getLongitude() == null) {
            return ResponseEntity.badRequest()
                    .body("Latitude and Longitude are required");
        }

// ✅ Optional: Range validation (VERY GOOD PRACTICE)
        if (request.getLatitude() < -90 || request.getLatitude() > 90 ||
                request.getLongitude() < -180 || request.getLongitude() > 180) {

            return ResponseEntity.badRequest()
                    .body("Invalid latitude/longitude values");
        }

// 👉 Continue your existing logic below
        String phone = authentication.getName();

        User user = userRepository
                .findByPhoneNumber(phone)
                .orElseThrow();

        ServiceProviderListing listing = listingRepository
                .findByLinkedUserId(user.getId())
                .orElseThrow();

        Address address = listing.getAddress();

        if (address == null) {
            address = new Address();
            address.setListing(listing);
        }

        address.setLocality(request.getLocality());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());

// ✅ SAVE LAT/LNG
        address.setLatitude(request.getLatitude());
        address.setLongitude(request.getLongitude());

        addressRepository.save(address);

        return ResponseEntity.ok("Address updated successfully");


    }


    @PostMapping("/submit-kyc")
    public ResponseEntity<?> submitKyc(@RequestBody KycRequest request,
                                       Authentication authentication) {

        String phone = authentication.getName();

        User provider = userRepository.findByPhoneNumber(phone).orElseThrow();

        ProviderVerification verification =
                verificationRepository.findByProvider(provider)
                        .orElse(new ProviderVerification());

        verification.setProvider(provider);
        verification.setAadharNumber(request.getAadharNumber());
        verification.setPanNumber(request.getPanNumber());
        verification.setVerifiedByAdmin(false);

        verificationRepository.save(verification);

        return ResponseEntity.ok("KYC submitted successfully. Waiting for admin verification.");
    }

    @GetMapping("/my-listing")
    public ProviderProfileResponse getMyListing(Authentication authentication) {

        String phone = authentication.getName();

        User provider = userRepository
                .findByPhoneNumber(phone)
                .orElseThrow();

        ServiceProviderListing listing =
                listingRepository
                        .findByLinkedUser(provider)
                        .orElseThrow();

        Address address = listing.getAddress();

        String locality = null;
        String city = null;

        if (address != null) {
            locality = address.getLocality();
            city = address.getCity();
        }

        return new ProviderProfileResponse(
                listing.getName(),
                listing.getServiceType() != null
                        ? listing.getServiceType().name()
                        : null,
                listing.getStatus().name(),
                locality,
                city
        );
    }
}