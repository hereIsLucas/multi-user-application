package com.example.multiuserapplication.dto;

/**
 * @author : lucas
 * @project : MultiUserApplication
 * @created : 12/07/2024, Friday
 **/
import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
