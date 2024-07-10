package com.example.multiuserapplication;

import jakarta.persistence.Id;
import lombok.Getter;

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

    public void setName(String name) {
        this.name = name;
    }
}
