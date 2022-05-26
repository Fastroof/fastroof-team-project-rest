package com.fastroof.ftpr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role_change_requests")
public class RoleChangeRequest {
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    @GeneratedValue(generator = "increment")
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "moderator_id")
    private Integer moderatorId;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "processed_at")
    private LocalDate processedAt;
}
