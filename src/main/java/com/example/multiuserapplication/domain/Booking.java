package com.example.multiuserapplication.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
/**
 * @author : lucas
 * @project : MultiUserApplication
 * @created : 10/07/2024, Wednesday
 **/
@Data
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "day_name", referencedColumnName = "name")
    private Day day;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Booking(Date date, Day day, Room room, User user) {
        this.date = date;
        this.day = day;
        this.room = room;
        this.user = user;
    }

    protected Booking() {

    }
}
