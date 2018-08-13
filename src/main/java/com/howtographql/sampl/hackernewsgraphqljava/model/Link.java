package com.howtographql.sampl.hackernewsgraphqljava.model;

import lombok.*;

import javax.persistence.Entity;

@Entity
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
