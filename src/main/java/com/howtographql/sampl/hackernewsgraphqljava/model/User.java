package com.howtographql.sampl.hackernewsgraphqljava.model;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    private String name;
    private String email;
    private String password;
}
