package com.example.multiuserapplication.controller;

import com.example.multiuserapplication.domain.Task;
import com.example.multiuserapplication.domain.TasksUser;
import com.example.multiuserapplication.dto.TaskDTO;
import com.example.multiuserapplication.dto.TasksUserDTO;
import com.example.multiuserapplication.mapper.TaskMapper;
import com.example.multiuserapplication.repositories.TaskRepository;
import com.example.multiuserapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
                          TaskMapper taskMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // User endpoints
    @PostMapping("/sign-up")
    public void signUp(@RequestBody TasksUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @GetMapping
    public ResponseEntity<List<TasksUserDTO>> getAllUsers() {
        List<TasksUser> users = userRepository.findAll();
        List<TasksUserDTO> userDTOs = taskMapper.mapUsersToDtoList(users);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody TasksUserDTO updatedUserDTO,
                                        @AuthenticationPrincipal TasksUser authenticatedUser) {
        if (!id.equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own data.");
        }

        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUserDTO.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(updatedUserDTO.getPassword()));
            user.setRoles(updatedUserDTO.getRoles());
            userRepository.save(user);
            return ResponseEntity.ok("User updated successfully.");
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found."));
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

    // Admin endpoints for user management

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<?> createUserAdmin(@RequestBody TasksUserDTO userDTO) {
        if (userRepository.findOneByUsername(userDTO.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }
        TasksUser user = taskMapper.mapUserDtoToEntity(userDTO);
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateUserAdmin(@PathVariable Long id, @RequestBody TasksUserDTO updatedUserDTO) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUserDTO.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(updatedUserDTO.getPassword()));
            user.setRoles(updatedUserDTO.getRoles());
            userRepository.save(user);
            return ResponseEntity.ok("User updated successfully.");
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found."));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteUserAdmin(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.ok().body("User deleted successfully.");
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found."));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<TasksUserDTO>> getAllUsersAdmin() {
        List<TasksUser> users = userRepository.findAll();
        List<TasksUserDTO> userDTOs = taskMapper.mapUsersToDtoList(users);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDTOs);
    }
}
