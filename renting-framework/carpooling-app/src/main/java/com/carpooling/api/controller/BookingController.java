package com.carpooling.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carpooling.api.dto.BookingCompletionDTO;
import com.carpooling.api.dto.BookingRequestDTO;
import com.carpooling.api.dto.BookingResponseDTO;
import com.carpooling.api.service.BookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO request) {
        return new ResponseEntity<>(bookingService.createBooking(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<BookingResponseDTO> completeBooking(
            @PathVariable Long id,
            @RequestBody BookingCompletionDTO request) {
        return ResponseEntity.ok(bookingService.completeBooking(id, request.getOdometerEnd()));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehicle/{listingId}")
    public ResponseEntity<List<BookingResponseDTO>> listByVehicle(@PathVariable Long listingId) {
        return ResponseEntity.ok(bookingService.listByVehicle(listingId));
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<BookingResponseDTO>> listByDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(bookingService.listByDriver(driverId));
    }
}