package com.nearserve.localservices.controller;

import com.nearserve.localservices.config.JwtService;
import com.nearserve.localservices.dto.LoginRequest;
import com.nearserve.localservices.dto.RegisterRequest;
import com.nearserve.localservices.entity.*;
import com.nearserve.localservices.repository.RoleRepository;
import com.nearserve.localservices.repository.ServiceProviderListingRepository;
import com.nearserve.localservices.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServiceProviderListingRepository listingRepository;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          ServiceProviderListingRepository listingRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.listingRepository = listingRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterRequest request) {

        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            return ResponseEntity.badRequest().body("Phone number already registered");
        }

        Role role = roleRepository.findByName("CUSTOMER").orElseThrow();

        User user = new User();
        user.setName(request.getName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(role));

        userRepository.save(user);

        return ResponseEntity.ok("Customer registered successfully");
    }

    @PostMapping("/register/provider")
    public ResponseEntity<?> registerProvider(@RequestBody RegisterRequest request) {

        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            return ResponseEntity.badRequest().body("Phone number already registered");
        }

        Role role = roleRepository.findByName("SERVICE_PROVIDER").orElseThrow();

        User user = new User();
        user.setName(request.getName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(role));

        userRepository.save(user);

        // 🔥 CLAIM LOGIC STARTS HERE
        var existingListing = listingRepository.findByPhoneNumber(request.getPhoneNumber());

        if (existingListing.isPresent()) {

            ServiceProviderListing listing = existingListing.get();
            listing.setLinkedUser(user);
            listing.setStatus(ListingStatus.PENDING_VERIFICATION);
            listingRepository.save(listing);

        } else {

            ServiceProviderListing newListing = new ServiceProviderListing();

            newListing.setName(request.getName());
            newListing.setPhoneNumber(request.getPhoneNumber());
            newListing.setDescription("Profile created by provider");
            if (request.getServiceType() != null) {
                newListing.setServiceType(
                        request.getServiceType()
                );
            }
            newListing.setStatus(ListingStatus.PENDING_VERIFICATION);
            newListing.setCreatedBy(null);
            newListing.setLinkedUser(user);

            listingRepository.save(newListing);
        }

        return ResponseEntity.ok("Service provider registered. Waiting for admin approval.");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {


// ✅ Prevent crash
        if (authentication == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String phone = authentication.getName();

        User user = userRepository
                .findByPhoneNumber(phone)
                .orElseThrow();

        return ResponseEntity.ok(
                new Object() {
                    public final String name = user.getName();
                    public final String phoneNumber = user.getPhoneNumber();
                    public final Set<Role> roles = user.getRoles();
                }
        );


    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        User user = userRepository
                .findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getPhoneNumber());

        return ResponseEntity.ok(token);
    }
}