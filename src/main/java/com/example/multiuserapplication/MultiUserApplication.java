package com.example.multiuserapplication;

import com.example.multiuserapplication.domain.Booking;
import com.example.multiuserapplication.domain.Day;
import com.example.multiuserapplication.domain.Room;
import com.example.multiuserapplication.domain.TasksUser;
import com.example.multiuserapplication.repositories.BookingRepository;
import com.example.multiuserapplication.repositories.DayRepository;
import com.example.multiuserapplication.repositories.RoomRepository;
import com.example.multiuserapplication.repositories.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
@SpringBootApplication
public class MultiUserApplication {

    @Bean
    public ApplicationRunner runner (UserRepository userRepository, RoomRepository roomRepository, BookingRepository bookingRepository, DayRepository dayRepository) {
        return args -> {
            var tasksUser = new TasksUser("SpongeBob", "Squarepants", "sponge@gmail.com", "Grafik" + "Designer");
            var room = new Room("411", 4, 200, List.of());
            var booking = new Booking(new Date(), new Day ("Allday", List.of()), room, tasksUser);

            dayRepository.save(new Day("Allday", List.of()));

            userRepository.save(tasksUser);
            roomRepository.save(room);
            bookingRepository.saveAndFlush(booking);
            userRepository.flush();
            bookingRepository.flush();
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(MultiUserApplication.class, args);
    }

}
