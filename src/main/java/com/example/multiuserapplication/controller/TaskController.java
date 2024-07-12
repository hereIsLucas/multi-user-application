package com.example.multiuserapplication.controller;


import com.example.multiuserapplication.domain.*;
import com.example.multiuserapplication.dto.TaskDTO;
import com.example.multiuserapplication.mapper.TaskMapper;
import com.example.multiuserapplication.repositories.TaskRepository;
import com.example.multiuserapplication.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

//@RestController
//@RequestMapping("/tasks")
public class TaskController {
    static Logger log = Logger.getAnonymousLogger();
    //@Autowired
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskController(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    // https://spring.io/guides/gs/rest-service-cors/
    // Aufgabe 2 – TasksList TaskDTO send!
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

    // Aufgabe 3 - Einzelner TaskDTO laden
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        if (taskRepository.findById(id).isPresent()) {
            // get the tsk is there !
            Task task = taskRepository.findById(id).get();
            TaskDTO taskDTO = taskMapper.mapTaskToDTO(task);

            return ResponseEntity
                    .status(HttpStatus.OK) // http 200 ok
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(taskDTO);
        } else {
            return ResponseEntity
                    .notFound()
                    .build(); // http 404
        }


    }

    // Aufgabe 4 - TaskDTO adding
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
/*
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    Task newTask(@RequestBody Task newTask) {
        log.info(newTask.toString());
        return taskRepository.save(newTask);
    }
*/
    // Aufgabe 5 - Task bearbeiten
    @PutMapping("/{id}")
//    @ResponseStatus(HttpStatus.RESET_CONTENT)
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

    // Aufgabe 6 - Task löschen
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

    // Aufgabe 7 - DTO-Tasksliste löschen
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
//https://www.springcloud.io/post/2022-02/spring-security-get-current-user/#gsc.tab=0
    // get authenticated user
    @GetMapping("/my-tasks")
    public ResponseEntity<List<TaskDTO>> getTasksForCurrentUser() {
        // Get the authenticated user's username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // find the current user
        if (userRepository.findOneByUsername(username).isPresent()) {
            TasksUser tasksUser = userRepository.findOneByUsername(username).get();
            log.info(tasksUser.toString());

            // Pass the username to the task service
            List<Task> tasks = taskRepository.findTasksByTasksUserId(tasksUser.getId());

            return ResponseEntity.ok( taskMapper.mapTasksToDtoList(tasks));
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404
                    .build(); // empty body
        }
    }

}
