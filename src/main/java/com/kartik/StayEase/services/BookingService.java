package com.kartik.StayEase.services;

import com.kartik.StayEase.dto.BookingRequestDTO;
import com.kartik.StayEase.dto.BookingResponseDTO;
import com.kartik.StayEase.entities.Booking;
import com.kartik.StayEase.entities.BookingStatus;
import com.kartik.StayEase.entities.Hotel;
import com.kartik.StayEase.entities.User;
import com.kartik.StayEase.repositories.BookingRepository;
import com.kartik.StayEase.repositories.HotelRepository;
import com.kartik.StayEase.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequest) {
        // Check if the user and hotel exist
        Optional<User> userOpt = userRepository.findById(bookingRequest.getUserId());
        Optional<Hotel> hotelOpt = hotelRepository.findById(bookingRequest.getHotelId());

        if (!userOpt.isPresent() || !hotelOpt.isPresent()) {
            throw new RuntimeException("User or Hotel not found");
        }

        Booking booking = new Booking();
        booking.setUser(userOpt.get());
        booking.setHotel(hotelOpt.get());
        booking.setBookingDate(bookingRequest.getBookingDate());
        booking.setStatus(BookingStatus.PENDING);

        booking = bookingRepository.save(booking);

        BookingResponseDTO responseDTO = new BookingResponseDTO();
        responseDTO.setId(booking.getId());
        responseDTO.setHotelId(booking.getHotel().getId());
        responseDTO.setUserId(booking.getUser().getId());
        responseDTO.setBookingDate(booking.getBookingDate());
        responseDTO.setStatus(booking.getStatus().toString());

        return responseDTO;
    }

    public BookingResponseDTO updateBookingStatus(Long bookingId, String status) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);

        if (!bookingOpt.isPresent()) {
            throw new RuntimeException("Booking not found");
        }

        Booking booking = bookingOpt.get();
        // Assume roles are validated in the controller
        if (status.equals("CONFIRMED")) {
            booking.setStatus(BookingStatus.CONFIRMED);
        } else if (status.equals("CANCELED")) {
            booking.setStatus(BookingStatus.CANCELED);
        }

        booking = bookingRepository.save(booking);

        BookingResponseDTO responseDTO = new BookingResponseDTO();
        responseDTO.setId(booking.getId());
        responseDTO.setHotelId(booking.getHotel().getId());
        responseDTO.setUserId(booking.getUser().getId());
        responseDTO.setBookingDate(booking.getBookingDate());
        responseDTO.setStatus(booking.getStatus().toString());

        return responseDTO;
    }
}
