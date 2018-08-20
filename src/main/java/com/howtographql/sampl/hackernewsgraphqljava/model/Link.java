package com.howtographql.sampl.hackernewsgraphqljava.model;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "links")
@Where(clause = "DELETED_AT IS NULL")
@Getter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Link extends BaseEntity {
    private String url;
    private String description;
    private Long userId;
}
