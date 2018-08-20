package com.howtographql.sampl.hackernewsgraphqljava.model;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(of = "id")
@MappedSuperclass
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@EntityListeners(BaseEntityListener.class)
// @EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_seq")
    @SequenceGenerator(
            name = "app_seq",
            sequenceName = "app_seq",
            allocationSize = 1
    )
    protected Long id;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    protected ZonedDateTime createdAt;

    @CreatedBy
    @Column(name = "CREATED_BY", nullable = false, updatable = false)
    protected Long createdBy;

    @LastModifiedDate
    @Column(name = "UPDATED_AT", nullable = false)
    private ZonedDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "UPDATED_BY", nullable = false)
    protected Long updatedBy;

    @Column(name = "DELETED_AT")
    private ZonedDateTime deletedAt;

    @Column(name = "DELETED_BY")
    protected Long deletedBy;
}
