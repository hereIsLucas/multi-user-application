package com.example.multiuserapplication.controllers;

/**
 * @author : lucas
 * @project : MultiUserApplication
 * @created : 12/07/2024, Friday
 **/

import com.example.multiuserapplication.domain.User;
import com.example.multiuserapplication.dto.UserRequest;
import com.example.multiuserapplication.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/users")
@Tag(name = "User Management", description = "Verwaltung von Mitgliedern durch Administratoren")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @Operation(summary = "Erstellt einen neuen Benutzer", description = "Erstellt einen neuen Benutzer basierend auf der UserRequest")
    public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return ResponseEntity.badRequest().body(null);
        }

        User user = new User(
                userRequest.getFirstName(),
                userRequest.getLastName(),
                userRequest.getEmail(),
                userRequest.getPassword()
        );

        user = userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @Operation(summary = "Listet alle Benutzer auf", description = "Ruft eine Liste aller Benutzer ab")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ruft einen Benutzer nach ID ab", description = "Ruft einen Benutzer basierend auf der ID ab")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.get());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualisiert einen Benutzer", description = "Aktualisiert einen bestehenden Benutzer basierend auf der ID")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = existingUser.get();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());

        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Löscht einen Benutzer", description = "Löscht einen bestehenden Benutzer basierend auf der ID")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

