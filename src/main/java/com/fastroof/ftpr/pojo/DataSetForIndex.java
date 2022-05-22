package com.fastroof.ftpr.pojo;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class DataSetForIndex {
    private int id;
    private String name;
    private String tagName;
    private String ownerName;
    private int fileCount;
}
