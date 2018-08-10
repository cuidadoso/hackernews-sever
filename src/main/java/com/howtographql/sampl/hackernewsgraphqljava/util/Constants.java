package com.howtographql.sampl.hackernewsgraphqljava.util;

import java.time.Instant;
import java.time.ZonedDateTime;

import static java.time.ZoneOffset.UTC;

public class Constants {
    public static final ZonedDateTime NOW = Instant.now().atZone(UTC);
}
