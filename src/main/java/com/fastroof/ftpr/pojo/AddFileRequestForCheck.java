package com.fastroof.ftpr.pojo;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class AddFileRequestForCheck {
    private int id;
    private String ownerName;
    private String linkToFile;
    private String name;
}
