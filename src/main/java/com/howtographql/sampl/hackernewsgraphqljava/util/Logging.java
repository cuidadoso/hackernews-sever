package com.howtographql.sampl.hackernewsgraphqljava.util;

import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;

@Log
@UtilityClass
public class Logging {
    public static void logInfo(String pattern, Object ... args) {
        log.info(String.format(pattern, args));
    }
}
