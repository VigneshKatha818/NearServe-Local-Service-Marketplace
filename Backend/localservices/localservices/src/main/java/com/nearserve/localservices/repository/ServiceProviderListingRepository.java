package com.nearserve.localservices.repository;

import com.nearserve.localservices.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceProviderListingRepository
        extends JpaRepository<ServiceProviderListing, Long> {


    Optional<ServiceProviderListing> findByPhoneNumber(String phoneNumber);

    Optional<ServiceProviderListing> findByLinkedUser(User user);

    Optional<ServiceProviderListing> findByLinkedUserId(Long userId);

    List<ServiceProviderListing> findByStatusIn(List<ListingStatus> statuses);

    List<ServiceProviderListing> findByStatus(ListingStatus status);


    @Query("""
   SELECT l FROM ServiceProviderListing l
   JOIN l.address a
   WHERE l.status = 'VERIFIED'
   AND LOWER(a.city) = LOWER(:city)
   """)
    List<ServiceProviderListing> findVerifiedByCity(@Param("city") String city);


    @Query("""
   SELECT l FROM ServiceProviderListing l
   JOIN l.address a
   WHERE l.status = 'VERIFIED'
   AND LOWER(a.locality) = LOWER(:locality)
   AND l.serviceType = :serviceType
   """)
    List<ServiceProviderListing> searchByLocalityAndService(
            @Param("locality") String locality,
            @Param("serviceType") ServiceType serviceType);


    // 🔥 FINAL FIXED QUERY
    @Query("""
   SELECT l,
          (6371 * acos(
                cos(radians(:lat)) *
                cos(radians(a.latitude)) *
                cos(radians(a.longitude) - radians(:lng)) +
                sin(radians(:lat)) *
                sin(radians(a.latitude))
          )) as distance
   FROM ServiceProviderListing l
   JOIN l.address a
   WHERE l.status = 'VERIFIED'
   AND l.serviceType = :service

    
   AND a.latitude IS NOT NULL
   AND a.longitude IS NOT NULL

   AND (
          6371 * acos(
                cos(radians(:lat)) *
                cos(radians(a.latitude)) *
                cos(radians(a.longitude) - radians(:lng)) +
                sin(radians(:lat)) *
                sin(radians(a.latitude))
          )
       ) <= :radius
   ORDER BY distance ASC
   """)
    List<Object[]> findNearbyProvidersWithDistance(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radius,
            @Param("service") ServiceType service);


}
