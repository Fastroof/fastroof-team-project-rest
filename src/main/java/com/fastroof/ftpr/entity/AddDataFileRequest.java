package com.fastroof.ftpr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "add_data_file_requests")
public class AddDataFileRequest {
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    @GeneratedValue(generator = "increment")
    private Integer id;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "link_to_file", nullable = false)
    private String linkToFile;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "moderator_id")
    private Integer moderatorId;

    @Column(name = "data_set_id", nullable = false)
    private Integer dataSetId;
}
