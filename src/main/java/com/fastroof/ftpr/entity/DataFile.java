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
@Table(name = "data_files")
public class DataFile {
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    @GeneratedValue(generator = "increment")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "data_set_id", nullable = false)
    private Integer dataSetId;

    @Column(name = "link_to_file", nullable = false)
    private String linkToFile;

    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;
}
