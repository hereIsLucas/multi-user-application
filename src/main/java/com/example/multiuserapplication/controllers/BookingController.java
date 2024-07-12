package com.example.multiuserapplication.controllers;

/**
 * @author : lucas
 * @project : MultiUserApplication
 * @created : 12/07/2024, Friday
 **/

import com.example.multiuserapplication.domain.Booking;
import com.example.multiuserapplication.domain.Day;
import com.example.multiuserapplication.domain.Room;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "User Booking", description = "Verwaltung der Buchungen durch Benutzer")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private DayRepository dayRepository;

    @PostMapping
    @Operation(summary = "Erstellt eine neue Buchung", description = "Erstellt eine neue Buchung basierend auf der BookingRequest")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Day day = dayRepository.findByName(bookingRequest.getDayName());
        Room room = roomRepository.findById(bookingRequest.getRoomId()).orElse(null);

        if (day == null || room == null) {
            return ResponseEntity.badRequest().build();
        }

        Booking booking = new Booking(
                bookingRequest.getDate(),
                day,
                room,
                user.get()
        );

        bookingRepository.save(booking);
        return ResponseEntity.ok(booking);
    }

    @GetMapping
    @Operation(summary = "Listet die Buchungen eines Benutzers auf", description = "Ruft eine Liste der Buchungen des authentifizierten Benutzers ab")
    public ResponseEntity<List<Booking>> getBookings(Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Booking> bookings = bookingRepository.findByUser(user.get());
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualisiert eine Buchung", description = "Aktualisiert eine bestehende Buchung basierend auf der ID")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody BookingRequest bookingRequest, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null || booking.getUser().getId() != user.get().getId()) {
            return ResponseEntity.notFound().build();
        }

        Day day = dayRepository.findByName(bookingRequest.getDayName());
        Room room = roomRepository.findById(bookingRequest.getRoomId()).orElse(null);

        if (day == null || room == null) {
            return ResponseEntity.badRequest().build();
        }

        booking.setDate(bookingRequest.getDate());
        booking.setDay(day);
        booking.setRoom(room);
        bookingRepository.save(booking);

        return ResponseEntity.ok(booking);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Löscht eine Buchung", description = "Löscht eine bestehende Buchung basierend auf der ID")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null || booking.getUser().getId() != user.get().getId()) {
            return ResponseEntity.notFound().build();
        }

        bookingRepository.delete(booking);
        return ResponseEntity.ok().build();
    }
}

