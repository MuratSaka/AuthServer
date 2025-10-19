package com.project.auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Murat Saka
 * @created 19/10/2025 - 11:10
 * @project AuthServer
 */
@Entity
@Data
public class Token {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UNIQUEIDENTIFIER")
    private UUID id;

    @Column(unique = true)
    private String token;

    @Column
    private Timestamp timestamp;

    @Column
    private LocalDateTime ExpiredAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private boolean isExpired;
}
