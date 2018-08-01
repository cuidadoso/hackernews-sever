package com.howtographql.sampl.hackernewsgraphqljava.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SigninPayload {
    private final Long token;
    private final User user;
}
