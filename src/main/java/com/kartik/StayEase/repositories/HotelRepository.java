package com.kartik.StayEase.repositories;



import com.kartik.StayEase.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByLocation(String location);

    List<Hotel> findByAvailableRoomsGreaterThanEqual(int availableRooms);

    List<Hotel> findByNameContainingIgnoreCase(String name);

    @Query("SELECT h FROM Hotel h WHERE h.location = :location AND h.availableRooms >= :availableRooms")
    List<Hotel> findByLocationAndAvailableRooms(@Param("location") String location, @Param("availableRooms") int availableRooms);
}

/**
 *
 * These methods will be automatically implemented by Spring Data JPA based on the method names or the custom queries we provide.
 */

