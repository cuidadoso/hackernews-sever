package com.howtographql.sampl.hackernewsgraphqljava.model;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Where(clause = "DELETED_AT IS NULL")
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
