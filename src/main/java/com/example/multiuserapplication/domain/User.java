package com.example.multiuserapplication.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
/**
 * @author : lucas
 * @project : MultiUserApplication
 * @created : 10/07/2024, Wednesday
 **/
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    protected User() {

    }
}
