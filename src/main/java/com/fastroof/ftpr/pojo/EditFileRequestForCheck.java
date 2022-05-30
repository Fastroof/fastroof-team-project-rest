package com.fastroof.ftpr.pojo;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@ToString

public class EditFileRequestForCheck {
    private int id;
    private String ownerName;
    private String linkToFile;
    private String name;
}
