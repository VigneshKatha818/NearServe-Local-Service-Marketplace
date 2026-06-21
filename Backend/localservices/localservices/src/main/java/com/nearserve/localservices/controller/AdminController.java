package com.nearserve.localservices.controller;

import com.nearserve.localservices.entity.*;
import com.nearserve.localservices.repository.ProviderVerificationRepository;
import com.nearserve.localservices.repository.ServiceProviderListingRepository;
import com.nearserve.localservices.dto.AdminListingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nearserve.localservices.repository.ProviderVerificationRepository;
import com.nearserve.localservices.entity.ProviderVerification;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ServiceProviderListingRepository listingRepository;
    private final ProviderVerificationRepository verificationRepository;

    public AdminController(ServiceProviderListingRepository listingRepository,
                           ProviderVerificationRepository verificationRepository) {

        this.listingRepository = listingRepository;
        this.verificationRepository = verificationRepository;
    }

    // View all pending listings
    @GetMapping("/pending-listings")
    public List<AdminListingResponse> getPendingListings() {

        List<ServiceProviderListing> listings =
                listingRepository.findByStatusIn(
                        List.of(
                                ListingStatus.UNCLAIMED,
                                ListingStatus.PENDING_VERIFICATION
                        )
                );

        return listings.stream()
                .map(listing -> {

                    String locality = null;
                    String city = null;
                    String state = null;
                    String pincode = null;

                    if (listing.getAddress() != null) {
                        locality = listing.getAddress().getLocality();
                        city = listing.getAddress().getCity();
                        state = listing.getAddress().getState();
                        pincode = listing.getAddress().getPincode();
                    }

                    ProviderVerification verification = null;

                    if (listing.getLinkedUser() != null) {
                        verification = verificationRepository
                                .findByProvider(listing.getLinkedUser())
                                .orElse(null);
                    }

                    String aadhar = verification != null ? verification.getAadharNumber() : null;
                    String pan = verification != null ? verification.getPanNumber() : null;

                    return new AdminListingResponse(
                            listing.getId(),
                            listing.getName(),
                            listing.getPhoneNumber(),
                            listing.getDescription(),
                            listing.getStatus().name(),
                            listing.getCreatedBy() != null
                                    ? listing.getCreatedBy().getName()
                                    : "Self Registered",
                            locality,
                            city,
                            state,
                            pincode,
                            aadhar,
                            pan
                    );
                })
                .toList();
    }

    // Approve listing
    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approveProvider(@PathVariable Long id) {

        ServiceProviderListing listing =
                listingRepository.findById(id).orElseThrow();

        // Check address
        if (listing.getAddress() == null) {
            return ResponseEntity.badRequest()
                    .body("Cannot approve provider. Address not added yet.");
        }

        // Check KYC
        if (listing.getLinkedUser() == null) {
            return ResponseEntity.badRequest()
                    .body("Cannot approve provider. Provider account missing.");
        }

        ProviderVerification verification =
                verificationRepository
                        .findByProvider(listing.getLinkedUser())
                        .orElse(null);

        if (verification == null) {
            return ResponseEntity.badRequest()
                    .body("Cannot approve provider. KYC not submitted.");
        }

        listing.setStatus(ListingStatus.VERIFIED);
        listingRepository.save(listing);

        return ResponseEntity.ok("Provider approved successfully");
    }

    // Reject listing
    @PostMapping("/reject/{id}")
    public ResponseEntity<?> rejectListing(@PathVariable Long id) {

        ServiceProviderListing listing =
                listingRepository.findById(id).orElseThrow();

        listing.setStatus(ListingStatus.REJECTED);
        listingRepository.save(listing);

        return ResponseEntity.ok("Listing rejected");
    }
}