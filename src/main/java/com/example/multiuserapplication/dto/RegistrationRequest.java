package com.example.multiuserapplication.dto;

/**
 * @author : lucas
 * @project : MultiUserApplication
 * @created : 12/07/2024, Friday
 **/
import lombok.Data;

@Data
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
