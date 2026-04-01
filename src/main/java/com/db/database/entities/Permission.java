package com.db.database.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String endpoint;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    // getters and setters
}