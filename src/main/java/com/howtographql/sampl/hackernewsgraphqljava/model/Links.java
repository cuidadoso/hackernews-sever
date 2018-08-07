package com.howtographql.sampl.hackernewsgraphqljava.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class Links {
    private final List<Link> items;
    private final PageInfo pageInfo;
}
