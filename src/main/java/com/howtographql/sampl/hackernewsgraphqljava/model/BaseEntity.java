package com.howtographql.sampl.hackernewsgraphqljava.model;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(exclude = "createdAt")
@MappedSuperclass
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@EntityListeners(BaseEntityListener.class)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected ZonedDateTime createdAt;
}
