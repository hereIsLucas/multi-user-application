package com.example.multiuserapplication.controller;

import com.example.multiuserapplication.domain.Task;
import com.example.multiuserapplication.domain.TasksUser;
import com.example.multiuserapplication.dto.TaskDTO;
import com.example.multiuserapplication.mapper.TaskMapper;
import com.example.multiuserapplication.repositories.TaskRepository;
import com.example.multiuserapplication.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks", description = "API für Aufgaben")
public class TaskController {
    static Logger log = Logger.getAnonymousLogger();
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskController(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    @Operation(summary = "Get all tasks")
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasks() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskDTO> taskDTOs = taskMapper.mapTasksToDtoList(tasks);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskDTOs);
    }

    @Operation(summary = "Get a task by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        if (taskRepository.findById(id).isPresent()) {
            Task task = taskRepository.findById(id).get();
            TaskDTO taskDTO = taskMapper.mapTaskToDTO(task);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(taskDTO);
        } else {
            return ResponseEntity
                    .notFound()
                    .build(); // http 404
        }
    }

    @Operation(summary = "Add a new task")
    @PostMapping()
    public ResponseEntity<TaskDTO> addTask(@RequestBody TaskDTO taskDTO) {
        Task task = taskMapper.mapsTaskDtoToEntity(taskDTO);
        Task savedTask = taskRepository.save(task);
        TaskDTO savedTaskDTO = taskMapper.mapTaskToDTO(savedTask);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedTaskDTO);
    }

    @Operation(summary = "Edit an existing task")
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> editTask(@PathVariable long id, @RequestBody TaskDTO taskDTO) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Task existingTask = optionalTask.get();
        Task updatedTask = taskMapper.mapsTaskDtoToEntity(taskDTO);
        updatedTask.setId(id);
        Task savedTask = taskRepository.save(updatedTask);
        TaskDTO savedTaskDTO = taskMapper.mapTaskToDTO(savedTask);

        return ResponseEntity.ok(savedTaskDTO);
    }

    @Operation(summary = "Delete a task by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDTO> deleteTask(@PathVariable long id) {
        if (taskRepository.findById(id).isPresent()) {
            taskRepository.deleteById(id);
            return ResponseEntity
                    .status(HttpStatus.RESET_CONTENT)
                    .build();
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)   // 404 not found
                    .build(); // empty body
        }
    }

    @Operation(summary = "Delete all tasks")
    @DeleteMapping("")
    public ResponseEntity<List<TaskDTO>> deleteAllTasks() {
        if (taskRepository.findAll().isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        } else {
            taskRepository.deleteAll();
            return ResponseEntity
                    .status(HttpStatus.RESET_CONTENT)
                    .build();
        }
    }

    @Operation(summary = "Get tasks for the authenticated user")
    @GetMapping("/my-tasks")
    public ResponseEntity<List<TaskDTO>> getTasksForCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (userRepository.findOneByUsername(username).isPresent()) {
            TasksUser tasksUser = userRepository.findOneByUsername(username).get();
            log.info(tasksUser.toString());

            List<Task> tasks = taskRepository.findTasksByTasksUserId(tasksUser.getId());

            return ResponseEntity.ok(taskMapper.mapTasksToDtoList(tasks));
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404
                    .build(); // empty body
        }
    }
}
