package com.howtographql.sampl.hackernewsgraphqljava.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ZonedDateTime createdAt;
    private String url;
    private String description;
    private Long userId;

    @Builder
    public Link(String url, String description) {
        this.url = url;
        this.description = description;
    }

    @Builder
    public Link(ZonedDateTime createdAt, String url, String description, Long userId) {
        this(null, createdAt, url, description, userId);
    }
}
