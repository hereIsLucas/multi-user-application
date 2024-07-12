package com.example.multiuserapplication.domain;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author Luigi Cavuoti
 *
 */
@Data
@Entity
@Table(name = "user")
public class TasksUser implements Serializable {
//	private static final long serialVersionUID = 5558730372383087546L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 50, unique = true, nullable = false)
	private String username;
	@Column(name = "hashed_password")
	private String password;
	private String salt;
	private String role;


	public TasksUser() {
		super();
	}

	public TasksUser(String login, String hashedPassword, String salt) {
		super();
		this.username = login;
		this.password = hashedPassword;
		this.salt = salt;
		this.role = TasksUserRoles.MEMBER;
	}

	public TasksUser(String login, String hashedPassword, String salt, String role) {
		super();
		this.username = login;
		this.password = hashedPassword;
		this.salt = salt;
		this.role = TasksUserRoles.MEMBER;
	}

	public void setRoles(Object roles) {
	}
}
