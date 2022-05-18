package com.fastroof.ftpr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "request_statuses")
public class RequestStatuses {
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    @GeneratedValue(generator = "increment")
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
