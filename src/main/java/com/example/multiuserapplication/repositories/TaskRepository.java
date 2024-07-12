package com.example.multiuserapplication.repositories;

import com.example.multiuserapplication.domain.Task;
import com.example.multiuserapplication.domain.TasksUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


// this creates a REST-Controller API for the Task entity and exposes it at /tasks
@RepositoryRestResource
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findTasksByTasksUserId(Long  userId);

    default Task save(Task task, TasksUser user) {
        task.setTasksUser(user);
        return save(task);
    }

    //    @Query("select t from Task t where t.user = :user")
    List<Task> findAllTasksByTasksUser(TasksUser user);
}
