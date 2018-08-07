package com.howtographql.sampl.hackernewsgraphqljava.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class PageInfo {
    private final boolean hasNextPage;
    private final boolean hasPreviousPage;
}
