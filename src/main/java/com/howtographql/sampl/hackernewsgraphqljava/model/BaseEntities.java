package com.howtographql.sampl.hackernewsgraphqljava.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BaseEntities<E extends BaseEntity> {
    private List<E> items;
    private PageInfo pageInfo;
}
