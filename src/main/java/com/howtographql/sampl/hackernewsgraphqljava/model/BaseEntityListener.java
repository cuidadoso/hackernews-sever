package com.howtographql.sampl.hackernewsgraphqljava.model;

import javax.persistence.PrePersist;

import static com.howtographql.sampl.hackernewsgraphqljava.util.Constants.NOW;

public class BaseEntityListener {
    @PrePersist
    public void onCreate(BaseEntity baseEntity) {
        baseEntity.setCreatedAt(NOW);
    }
}
