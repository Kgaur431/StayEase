package com.kartik.StayEase.controllers;

import com.kartik.StayEase.dto.HotelRequestDTO;
import com.kartik.StayEase.dto.HotelResponseDTO;
import com.kartik.StayEase.entities.Hotel;
import com.kartik.StayEase.services.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    // Add a new hotel
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HotelResponseDTO> addHotel(@RequestBody HotelRequestDTO hotelRequestDTO) {
        HotelResponseDTO createdHotel = hotelService.addHotel(hotelRequestDTO);
        return new ResponseEntity<>(createdHotel, HttpStatus.CREATED);
    }

    // Update an existing hotel
    @PutMapping("/{id}")
    public ResponseEntity<HotelResponseDTO> updateHotel(@PathVariable Long id, @RequestBody HotelRequestDTO hotelRequestDTO) {
        try {
            HotelResponseDTO updatedHotel = hotelService.updateHotel(id, hotelRequestDTO);
            return ResponseEntity.ok(updatedHotel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Get all hotels
    @GetMapping
    public ResponseEntity<List<HotelResponseDTO>> getAllHotels() {
        List<HotelResponseDTO> hotels = hotelService.getAllHotels();
        if (hotels.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(hotels);
    }


    // Get a hotel by its id
    @GetMapping("/{id}")
    public ResponseEntity<HotelResponseDTO> getHotelById(@PathVariable Long id) {
        try {
            HotelResponseDTO hotel = hotelService.getHotelById(id);
            return ResponseEntity.ok(hotel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Delete a hotel
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id) {
        try {
            hotelService.deleteHotel(id);
            return ResponseEntity.ok("Hotel deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotel not found with id: " + id);
        }
    }

    // Endpoint to find hotels by location
    @GetMapping("/location/{location}")
    public ResponseEntity<List<HotelResponseDTO>> getHotelsByLocation(@PathVariable String location) {
        List<HotelResponseDTO> hotels = hotelService.findByLocation(location);
        if (hotels.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(hotels);
    }

    // Endpoint to find hotels with available rooms greater than or equal to a specified number
    @GetMapping("/available-rooms")
    public ResponseEntity<List<HotelResponseDTO>> getHotelsByAvailableRooms(@RequestParam int availableRooms) {
        List<HotelResponseDTO> hotels = hotelService.findByAvailableRoomsGreaterThanEqual(availableRooms);
        if (hotels.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(hotels);
    }

    // Endpoint to find hotels by name (case-insensitive)
    @GetMapping("/name")
    public ResponseEntity<List<HotelResponseDTO>> getHotelsByName(@RequestParam String name) {
        List<HotelResponseDTO> hotels = hotelService.findByNameContainingIgnoreCase(name);
        if (hotels.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(hotels);
    }

    // Endpoint to find hotels by location and available rooms
    @GetMapping("/location-rooms")
    public ResponseEntity<List<HotelResponseDTO>> getHotelsByLocationAndAvailableRooms(@RequestParam String location, @RequestParam int availableRooms) {
        List<HotelResponseDTO> hotels = hotelService.findByLocationAndAvailableRooms(location, availableRooms);
        if (hotels.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(hotels);
    }
}

