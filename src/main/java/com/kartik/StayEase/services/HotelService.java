package com.kartik.StayEase.services;

import com.kartik.StayEase.dto.HotelRequestDTO;
import com.kartik.StayEase.dto.HotelResponseDTO;
import com.kartik.StayEase.entities.Hotel;
import com.kartik.StayEase.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    // Add a new hotel
    public HotelResponseDTO addHotel(HotelRequestDTO hotelRequestDTO) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelRequestDTO.getName());
        hotel.setLocation(hotelRequestDTO.getLocation());
        hotel.setDescription(hotelRequestDTO.getDescription());
        hotel.setAvailableRooms(hotelRequestDTO.getAvailableRooms());
        hotel.setUpdatedDate(hotelRequestDTO.getUpdatedDate());
        hotel = hotelRepository.save(hotel);
        return convertToDTO(hotel);
    }

    // Update an existing hotel
    public HotelResponseDTO updateHotel(Long id, HotelRequestDTO hotelRequestDTO) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
        if (hotelOptional.isPresent()) {
            Hotel hotel = hotelOptional.get();
            hotel.setName(hotelRequestDTO.getName());
            hotel.setLocation(hotelRequestDTO.getLocation());
            hotel.setDescription(hotelRequestDTO.getDescription());
            hotel.setAvailableRooms(hotelRequestDTO.getAvailableRooms());
            hotel.setUpdatedDate(hotelRequestDTO.getUpdatedDate());
            hotel = hotelRepository.save(hotel);
            return convertToDTO(hotel);
        } else {
            throw new RuntimeException("Hotel not found with id: " + id);
        }
    }

    // Get all hotels
    public List<HotelResponseDTO> getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        return hotels.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get a hotel by its id
    public HotelResponseDTO getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));
        return convertToDTO(hotel);
    }

    // Delete a hotel
    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new RuntimeException("Hotel not found with id: " + id);
        }
        hotelRepository.deleteById(id);
    }

    // Find hotels by location
    public List<HotelResponseDTO> findByLocation(String location) {
        List<Hotel> hotels = hotelRepository.findByLocation(location);
        return hotels.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Find hotels with available rooms greater than or equal to the specified number
    public List<HotelResponseDTO> findByAvailableRoomsGreaterThanEqual(int availableRooms) {
        List<Hotel> hotels = hotelRepository.findByAvailableRoomsGreaterThanEqual(availableRooms);
        return hotels.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    // Find hotels by name (case-insensitive)
    public List<HotelResponseDTO> findByNameContainingIgnoreCase(String name) {
        List<Hotel> hotels = hotelRepository.findByNameContainingIgnoreCase(name);
        return hotels.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Fetch hotels by location and available rooms
    public List<HotelResponseDTO> findByLocationAndAvailableRooms(String location, int availableRooms) {
        List<Hotel> hotels = hotelRepository.findByLocationAndAvailableRooms(location, availableRooms);
        return hotels.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Convert Hotel to HotelResponseDTO
    private HotelResponseDTO convertToDTO(Hotel hotel) {
        HotelResponseDTO dto = new HotelResponseDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setLocation(hotel.getLocation());
        dto.setDescription(hotel.getDescription());
        dto.setAvailableRooms(hotel.getAvailableRooms());
        dto.setUpdatedDate(hotel.getUpdatedDate());
        return dto;
    }
}
