package com.example;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberRequest {
    private String email;
    private String name;
    private String password;
}