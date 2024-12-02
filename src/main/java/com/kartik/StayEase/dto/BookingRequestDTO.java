package com.kartik.StayEase.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class BookingRequestDTO {

    private Long hotelId;
    private Long userId;
    private LocalDateTime bookingDate;

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
