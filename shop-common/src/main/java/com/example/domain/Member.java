package com.example.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String loginId;

    private String password;
    private String name;
    private String email;
    private String phone;

    //  권한을 저장할 필드 (기본값 USER)
    @Builder.Default
    private String role = "USER";
}