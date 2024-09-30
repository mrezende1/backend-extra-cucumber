package org.example.model;

import lombok.Data;

@Data
public class User {
    private String id;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;
}
