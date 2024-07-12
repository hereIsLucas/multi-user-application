package com.example.multiuserapplication.controller;

/**
 * @author : lucas
 * @project : MultiUserApplication
 * @created : 12/07/2024, Friday
 **/
import com.example.multiuserapplication.domain.Booking;
import com.example.multiuserapplication.domain.TasksUser;
import com.example.multiuserapplication.repositories.BookingRepository;
import com.example.multiuserapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingController(BookingRepository bookingRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    // Get all bookings for the authenticated user
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings(@AuthenticationPrincipal TasksUser authenticatedUser) {
        List<Booking> bookings = bookingRepository.findByUserId(authenticatedUser.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(bookings);
    }

    // Create a new booking for the authenticated user
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking, @AuthenticationPrincipal TasksUser authenticatedUser) {
        booking.setUser(authenticatedUser);
        Booking savedBooking = bookingRepository.save(booking);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedBooking);
    }

    // Update an existing booking for the authenticated user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking, @AuthenticationPrincipal TasksUser authenticatedUser) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);

        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            if (!booking.getUser().getId().equals(authenticatedUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own bookings.");
            }
            booking.setRoom(updatedBooking.getRoom());
            booking.setDay(updatedBooking.getDay());
            Booking savedBooking = bookingRepository.save(booking);
            return ResponseEntity.ok(savedBooking);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found.");
        }
    }

    // Delete a booking for the authenticated user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id, @AuthenticationPrincipal TasksUser authenticatedUser) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);

        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            if (!booking.getUser().getId().equals(authenticatedUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own bookings.");
            }
            bookingRepository.delete(booking);
            return ResponseEntity.ok().body("Booking deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found.");
        }
    }

    // Admin endpoints

    // Get all bookings (admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<Booking>> getAllBookingsAdmin() {
        List<Booking> bookings = bookingRepository.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(bookings);
    }

    // Create a new booking for any user (admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<String> createBookingAdmin(@RequestBody Booking booking) {
        Optional<TasksUser> optionalUser = userRepository.findById(booking.getUser().getId());
        if (optionalUser.isPresent()) {
            Booking savedBooking = bookingRepository.save(booking);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(String.valueOf(savedBooking));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    // Update any booking (admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateBookingAdmin(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);

        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            booking.setRoom(updatedBooking.getRoom());
            booking.setDay(updatedBooking.getDay());
            booking.setUser(updatedBooking.getUser());
            Booking savedBooking = bookingRepository.save(booking);
            return ResponseEntity.ok(savedBooking);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found.");
        }
    }

    // Delete any booking (admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteBookingAdmin(@PathVariable Long id) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);

        if (optionalBooking.isPresent()) {
            bookingRepository.delete(optionalBooking.get());
            return ResponseEntity.ok().body("Booking deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found.");
        }
    }
}
