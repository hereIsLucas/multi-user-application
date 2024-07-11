package com.example.multiuserapplication;

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
public class Day {
    // Getter und Setter
    @Id
    private String name;

    @OneToMany(mappedBy = "day")
    private List<Booking> bookings;

    public void setName(String name) {
        this.name = name;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
