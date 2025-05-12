package com.company.project.type;

public enum BugServerity {
    LOW (0),
    MEDIUM (1),
    MAJOR(2),
    CRITICAL(3);

    private final int value;

    BugServerity(int value) {
        this.value = value;
    }

    public static boolean isValid(int value) {
        for (BugServerity item : BugServerity.values()) {
            if (item.ordinal() == value) {
                return true;
            }
        }
        return false;
    }
}