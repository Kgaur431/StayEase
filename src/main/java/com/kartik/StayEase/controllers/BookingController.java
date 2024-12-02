package com.kartik.StayEase.controllers;

import com.kartik.StayEase.dto.BookingRequestDTO;
import com.kartik.StayEase.dto.BookingResponseDTO;
import com.kartik.StayEase.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody BookingRequestDTO bookingRequest) {
        BookingResponseDTO bookingResponse = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(bookingResponse);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('HOTEL_MANAGER')")
    @PutMapping("/{bookingId}/status")
    public ResponseEntity<BookingResponseDTO> updateBookingStatus(@PathVariable Long bookingId, @RequestParam String status) {
        BookingResponseDTO bookingResponse = bookingService.updateBookingStatus(bookingId, status);
        return ResponseEntity.ok(bookingResponse);
    }
}
