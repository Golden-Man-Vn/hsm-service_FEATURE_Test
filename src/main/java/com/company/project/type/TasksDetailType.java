package com.company.project.type;

public enum TasksDetailType {
    BUG (0),
    FEATURE (1);

    private final int value;

    TasksDetailType(int value) {
        this.value = value;
    }

    public static boolean isValid(int value) {
        for (TasksDetailType item : TasksDetailType.values()) {
            if (item.ordinal() == value) {
                return true;
            }
        }
        return false;
    }
}
