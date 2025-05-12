package com.company.project.type;

public enum TasksStatus {
    OPEN (0),
    IN_PROGRESS (1),
    DONE(2);

    private final int value;

    TasksStatus(int value) {
        this.value = value;
    }

    public static boolean isValid(int value) {
        for (TasksStatus item : TasksStatus.values()) {
            if (item.ordinal() == value) {
                return true;
            }
        }
        return false;
    }

    public static TasksStatus fromValue(int value) {
        for (TasksStatus status : TasksStatus.values()) {
            if (status.ordinal() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}