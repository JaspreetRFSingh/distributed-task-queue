package com.taskqueue.util;

import java.util.UUID;

public class TaskIdGenerator {
    public static UUID generate() {
        return UUID.randomUUID();
    }
    
    public static String generateShortId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
