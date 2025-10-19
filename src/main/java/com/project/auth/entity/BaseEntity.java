package com.project.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

/**
 * @author Murat Saka
 * @created 18/10/2025 - 12:56
 * @project AuthServer
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Audited
public class BaseEntity {
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name= "created_by")
    private Long createdBy;

    @Column(name= "updated_by")
    private Long updatedBy;


}

