package com.timothy.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/*
CREATE TABLE user_account(
    real_name VARCHAR(40) NOT NULL,
    nickname VARCHAR(100) NULL,
    birthday DATE NOT NULL,
    gender VARCHAR(20) NULL,
    id VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    PRIMARY KEY(id)
);
 */
@Entity
@Table(name = "user_account")
@Data
@NoArgsConstructor
public class TKUserEntity implements Serializable {
    @Column(name = "real_name", nullable = false)
    private String realName;
    private String nickname;
    @Column(nullable = false)
    private LocalDate birthday;
    private String gender;
    @Id
    private String id;
    @Column(nullable = false)
    private String password;
}