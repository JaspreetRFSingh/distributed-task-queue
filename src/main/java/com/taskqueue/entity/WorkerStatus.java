package com.taskqueue.entity;

public enum WorkerStatus {
    ACTIVE,
    IDLE,
    BUSY,
    UNHEALTHY,
    DRAINING,
    OFFLINE
}
