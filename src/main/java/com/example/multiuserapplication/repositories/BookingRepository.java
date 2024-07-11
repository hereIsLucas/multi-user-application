package com.example.multiuserapplication.repositories;

import com.example.multiuserapplication.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : lucas
 * @project : MultiUserApplication
 * @created : 11/07/2024, Thursday
 **/
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
