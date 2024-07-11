package com.example.multiuserapplication.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import java.util.List;
/**
 * @author : lucas
 * @project : MultiUserApplication
 * @created : 10/07/2024, Wednesday
 **/
@Entity
@Data
public class Day {
    @Id
    private String name;

    @OneToMany(mappedBy = "day")
    private List<Booking> bookings;

    public Day(String name, List<Booking> bookings) {
        this.name = name;
        this.bookings = bookings;
    }

    protected Day() {

    }
}