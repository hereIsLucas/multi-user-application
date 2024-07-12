package com.example.multiuserapplication.controller;

import com.example.multiuserapplication.domain.Task;
import com.example.multiuserapplication.domain.TasksUser;
import com.example.multiuserapplication.dto.TaskDTO;
import com.example.multiuserapplication.dto.TasksUserDTO;
import com.example.multiuserapplication.mapper.TaskMapper;
import com.example.multiuserapplication.repositories.TaskRepository;
import com.example.multiuserapplication.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "API für Benutzerverwaltung")
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

    @Operation(summary = "Benutzer registrieren")
    @PostMapping("/sign-up")
    public void signUp(@RequestBody TasksUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Operation(summary = "Alle Benutzer abrufen")
    @GetMapping
    public ResponseEntity<List<TasksUserDTO>> getAllUsers() {
        List<TasksUser> users = userRepository.findAll();
        List<TasksUserDTO> userDTOs = taskMapper.mapUsersToDtoList(users);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDTOs);
    }

    @Operation(summary = "Benutzerdaten aktualisieren")
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

    @Operation(summary = "Neue Aufgabe für einen Benutzer erstellen")
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

    @Operation(summary = "Alle Aufgaben eines Benutzers abrufen")
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

    @Operation(summary = "Aktuellen Benutzernamen abrufen (Principal)")
    @GetMapping("/currentusername")
    public String currentUserName(Principal principal) {
        return principal.getName();
    }

    @Operation(summary = "Aktuellen Benutzernamen abrufen (Authentication)")
    @GetMapping("/currentusernameAuth")
    public String currentUserName(Authentication authentication) {
        return authentication.getName();
    }

    @Operation(summary = "Aktuellen Benutzernamen abrufen (AuthenticationPrincipal)")
    @GetMapping("/currentusernameAuthPrinc")
    public String currentUserName(@AuthenticationPrincipal TasksUser customUser) {
        return customUser.getUsername();
    }

    @Operation(summary = "Neuen Benutzer als Administrator erstellen")
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

    @Operation(summary = "Benutzerdaten als Administrator aktualisieren")
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

    @Operation(summary = "Benutzer als Administrator löschen")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteUserAdmin(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.ok().body("User deleted successfully.");
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found."));
    }

    @Operation(summary = "Alle Benutzer als Administrator abrufen")
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
