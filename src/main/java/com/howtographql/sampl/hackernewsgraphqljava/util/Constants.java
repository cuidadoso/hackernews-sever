package com.howtographql.sampl.hackernewsgraphqljava.util;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.ZonedDateTime;

import static java.time.ZoneOffset.UTC;

@UtilityClass
public class Constants {
    public static final ZonedDateTime NOW = Instant.now().atZone(UTC);
}
