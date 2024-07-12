package com.example.multiuserapplication.repositories;

import com.example.multiuserapplication.domain.TasksUser;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

/**
 * @author : lucas
 * @project : MultiUserApplication
 * @created : 11/07/2024, Thursday
 **/
public interface UserRepository extends JpaRepository<TasksUser, Long> {
    Optional<TasksUser> findOneByUsername(String login);
    Optional<TasksUser> findById(long id);
    List<TasksUser> findAll();
}
