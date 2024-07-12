package com.example.multiuserapplication.controllers;

/**
 * @author : lucas
 * @project : MultiUserApplication
 * @created : 12/07/2024, Friday
 **/
import com.example.multiuserapplication.domain.Booking;
import com.example.multiuserapplication.domain.User;
import com.example.multiuserapplication.dto.BookingRequest;
import com.example.multiuserapplication.repositories.BookingRepository;
import com.example.multiuserapplication.repositories.DayRepository;
import com.example.multiuserapplication.repositories.RoomRepository;
import com.example.multiuserapplication.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/bookings")
@Tag(name = "Admin Booking", description = "Verwaltung der Buchungen durch Administratoren")
public class AdminBookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private DayRepository dayRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @Operation(summary = "Erstellt eine neue Buchung", description = "Erstellt eine neue Buchung basierend auf der BookingRequest")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) {
        Optional<User> user = userRepository.findById(bookingRequest.getUserId());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build(); // User not found
        }

        Booking booking = new Booking(
                bookingRequest.getDate(),
                dayRepository.findByName(bookingRequest.getDayName()),
                roomRepository.findById(bookingRequest.getRoomId()).orElse(null),
                user.get()
        );

        bookingRepository.save(booking);
        return ResponseEntity.ok(booking);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    @Operation(summary = "Listet alle Buchungen auf", description = "Ruft eine Liste aller Buchungen ab")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return ResponseEntity.ok(bookings);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Aktualisiert eine Buchung", description = "Aktualisiert eine bestehende Buchung basierend auf der ID")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }

        booking.setDate(bookingRequest.getDate());
        booking.setDay(dayRepository.findByName(bookingRequest.getDayName()));
        booking.setRoom(roomRepository.findById(bookingRequest.getRoomId()).orElse(null));
        bookingRepository.save(booking);

        return ResponseEntity.ok(booking);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Löscht eine Buchung", description = "Löscht eine bestehende Buchung basierend auf der ID")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }

        bookingRepository.delete(booking);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/accept")
    @Operation(summary = "Akzeptiert eine Buchung", description = "Akzeptiert eine bestehende Buchung basierend auf der ID")
    public ResponseEntity<Booking> acceptBooking(@PathVariable Long id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }

        booking.setStatus("Accepted");
        bookingRepository.save(booking);

        return ResponseEntity.ok(booking);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/reject")
    @Operation(summary = "Lehnt eine Buchung ab", description = "Lehnt eine bestehende Buchung basierend auf der ID ab")
    public ResponseEntity<Booking> rejectBooking(@PathVariable Long id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }

        booking.setStatus("Rejected");
        bookingRepository.save(booking);

        return ResponseEntity.ok(booking);
    }
}

