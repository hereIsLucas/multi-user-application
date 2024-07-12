package com.example.multiuserapplication.mapper;

import com.example.multiuserapplication.domain.Task;
import com.example.multiuserapplication.domain.TasksUser;
import com.example.multiuserapplication.dto.TaskDTO;
import com.example.multiuserapplication.dto.TasksUserDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    // from DTO to entity
    public Task mapsTaskDtoToEntity(TaskDTO taskDTO) {
        TasksUser tasksUser = mapUserDtoToEntity(taskDTO.userDTO());
        return new Task(tasksUser, taskDTO.description());
    }

    // from entity to dto , task and user
    public TaskDTO mapTaskToDTO(Task task) {
        TasksUserDTO userDTO = mapUserToDTO(task.getTasksUser());
//        return new TaskDTO(task.getId(), task.getDescription(), userDTO);
        return TaskDTO.builder()
                .id(task.getId())
                .description(task.getDescription())
                .userDTO(userDTO)
                .build();
    }
    // list of tasks
    public List<TaskDTO> mapTasksToDtoList(List<Task> tasks) {
        return tasks.stream()
                .map(this::mapTaskToDTO)
                .collect(Collectors.toList());
    }

    // user mapping
    public TasksUser mapUserDtoToEntity(TasksUserDTO userDTO) {
        TasksUser tasksUser = new TasksUser();
        tasksUser.setId(userDTO.id());
        tasksUser.setUsername(userDTO.username());
        tasksUser.setPassword(userDTO.password());
        return tasksUser;
    }
    // list of users
    public List<TasksUserDTO> mapUsersToDtoList(List<TasksUser> users) {
        return users.stream()
                .map(this::mapUserToDTO)
                .collect(Collectors.toList());
    }
    public TasksUserDTO mapUserToDTO(TasksUser tasksUser) {
        return new TasksUserDTO(tasksUser.getId(), tasksUser.getUsername(), tasksUser.getPassword(), tasksUser.getSalt(), tasksUser.getRole());
    }

}
