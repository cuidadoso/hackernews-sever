package com.howtographql.sampl.hackernewsgraphqljava.model;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import static com.howtographql.sampl.hackernewsgraphqljava.configurations.SpringBeanUtils.session;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Constants.NOW;

public class BaseEntityListener {
    @PrePersist
    public void onCreate(BaseEntity baseEntity) {
        baseEntity.setCreatedAt(NOW);
        baseEntity.setCreatedBy(session().userId());
        baseEntity.setUpdatedAt(NOW);
        baseEntity.setUpdatedBy(session().userId());
    }

    @PreUpdate
    public void onUpdate(BaseEntity baseEntity) {
        baseEntity.setUpdatedAt(NOW);
        baseEntity.setUpdatedBy(session().userId());
    }
}
