package com.fastroof.ftpr.pojo;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class EditDatasetFormData implements Serializable {
    private String name;
    private String tagName;
    private MultipartFile[] fileIn;
    private String[] oldFileName;
}
