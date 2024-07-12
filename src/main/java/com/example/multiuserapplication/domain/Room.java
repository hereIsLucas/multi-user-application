package com.example.multiuserapplication.domain;
/**
 * @author : lucas
 * @project : MultiUserApplication
 * @created : 10/07/2024, Wednesday
 **/

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private int floor;
    private double price;

    @OneToMany(mappedBy = "room")
    private List<Booking> bookings;

    public Room( String name, int floor, double price, List<Booking> bookings) {
        this.name = name;
        this.floor = floor;
        this.price = price;
        this.bookings = bookings;
    }

    protected Room() {

    }
}
