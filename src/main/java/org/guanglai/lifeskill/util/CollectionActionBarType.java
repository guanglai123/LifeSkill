package org.guanglai.lifeskill.util;

public enum CollectionActionBarType {
    COUNTDOWN,
    PROGRESS,
    NONE,
    INVALID;

    public static CollectionActionBarType safeValueOf(String name) {
        if (name != null) {
            try {
                return valueOf(name);
            } catch (IllegalArgumentException e) {
                return INVALID;
            }
        } else {
            return INVALID;
        }
    }
}
