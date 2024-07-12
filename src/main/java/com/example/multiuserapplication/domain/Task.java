package com.example.multiuserapplication.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;

    @ManyToOne
    @JoinColumn(name="user_id")
    private TasksUser tasksUser;

    protected Task() { }
    public Task(TasksUser tasksUser, String description) {
        this.tasksUser = tasksUser;
        this.description = description;
    }
    public Task(String description) {
        this.description = description;
    }

}
