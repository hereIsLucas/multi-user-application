package com.example.multiuserapplication.repositories;

import com.example.multiuserapplication.domain.Day;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : lucas
 * @project : MultiUserApplication
 * @created : 11/07/2024, Thursday
 **/
public interface DayRepository extends JpaRepository<Day, String> {
}
