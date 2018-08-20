package com.howtographql.sampl.hackernewsgraphqljava.util;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.ZonedDateTime;

import static java.time.ZoneOffset.UTC;

@UtilityClass
public class Constants {
    public static final ZonedDateTime NOW = Instant.now().atZone(UTC);

    public final static long USER_ID = 1000L;

    public static final String UID = "uid";

    public static final String NULL = "NULL";
}
