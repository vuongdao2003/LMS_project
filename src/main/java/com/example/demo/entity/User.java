package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "username", nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    private String username;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "first_name", columnDefinition = "NVARCHAR(255)")
    private String firstName;
    @Column(name = "last_name", columnDefinition = "NVARCHAR(255)")
    private String lastName;

    @ManyToMany
    private Set<Role> roles;
}
