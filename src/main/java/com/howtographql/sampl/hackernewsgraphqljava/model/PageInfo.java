package com.howtographql.sampl.hackernewsgraphqljava.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class PageInfo {
    private final boolean hasNextPage;
    private final boolean hasPreviousPage;
    private final Long total;
    private final int totalPages;
}
