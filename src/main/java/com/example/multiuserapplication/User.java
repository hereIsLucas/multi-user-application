package com.example.multiuserapplication;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.List;

/**
 * @author : lucas
 * @project : MultiUserApplication
 * @created : 10/07/2024, Wednesday
 **/
@Getter
public class User {
    // Getter und Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
