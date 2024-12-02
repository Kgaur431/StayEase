package com.kartik.StayEase.repositories;

import com.kartik.StayEase.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId); // To find bookings for a specific user
    List<Booking> findByHotelId(Long hotelId); // To find bookings for a specific hotel
}

