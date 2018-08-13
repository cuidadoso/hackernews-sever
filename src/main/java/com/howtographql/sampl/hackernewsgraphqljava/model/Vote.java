package com.howtographql.sampl.hackernewsgraphqljava.model;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Vote extends BaseEntity {
    private Long userId;
    private Long linkId;
}
