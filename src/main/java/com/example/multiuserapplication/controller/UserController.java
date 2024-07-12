package com.example.multiuserapplication.controller;


import com.example.multiuserapplication.domain.*;
import com.example.multiuserapplication.repositories.TaskRepository;
import com.example.multiuserapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final TaskMapper taskMapper;


    @Autowired
    public UserController(UserRepository userRepository, TaskRepository taskRepository,
                          TaskMapper taskMapper,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        super();
        this.userRepository = userRepository;
        this.taskRepository =taskRepository;
        this.taskMapper = taskMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    }
    // User endpoints
    @PostMapping("/sign-up")
    public void signUp(@RequestBody TasksUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);

    }

 /*   @PostMapping("/sign-up")
    public ResponseEntity<TasksUserDTO> signUp(@RequestBody TasksUserDTO userDTO) {

        TasksUser user = taskMapper.mapUserDtoToEntity(userDTO);

        Optional<TasksUser> optionalUser = userRepository.findOneByUsername(userDTO.username());

        if (optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        user.setPassword(bCryptPasswordEncoder.encode(userDTO.password()));
        TasksUser registeredUser = userRepository.save(user);
        TasksUserDTO registeredUserDTO = taskMapper.mapUserToDTO(registeredUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(registeredUserDTO);

    }*/
    @GetMapping
    public ResponseEntity<List<TasksUserDTO>> getAllUsers() {

        List<TasksUser> users = userRepository.findAll();
        List<TasksUserDTO> userDTOs = taskMapper.mapUsersToDtoList(users);


        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDTOs);


    }
    // Tasks Endpoint
    @PostMapping("/{userId}/tasks")
    public ResponseEntity<TaskDTO> createUserTask(@PathVariable Long userId, @RequestBody TaskDTO taskDTO) {
        Optional<TasksUser> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {

            TasksUser user = optionalUser.get();
            Task task = taskMapper.mapsTaskDtoToEntity(taskDTO);

            task.setTasksUser(user);
            Task savedTask = taskRepository.save(task);
            TaskDTO savedTaskDTO = taskMapper.mapTaskToDTO(savedTask);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(savedTaskDTO);

        } else {
            return ResponseEntity.notFound().build();
        }


    }

    @GetMapping("/{userId}/tasks")
    public ResponseEntity<Iterable<TaskDTO>> getUserTasks(@PathVariable Long userId) {
        Optional<TasksUser> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            TasksUser user = optionalUser.get();

            List<Task> tasks = taskRepository.findTasksByTasksUserId(user.getId());
            List<TaskDTO> taskDTOs = taskMapper.mapTasksToDtoList(tasks);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(taskDTOs);

        } else {
            return ResponseEntity.notFound().build();
        }

    }

//https://www.springcloud.io/post/2022-02/spring-security-get-current-user/#gsc.tab=0
    @GetMapping("/currentusername")
    public String currentUserName(Principal principal) {
        return principal.getName();
    }
    @GetMapping("/currentusernameAuth")
    public String currentUserName(Authentication authentication) {
        return authentication.getName();
    }
    @GetMapping("/currentusernameAuthPrinc")
    public String currentUserName(@AuthenticationPrincipal TasksUser customUser) {
        return customUser.getUsername();
    }

}
