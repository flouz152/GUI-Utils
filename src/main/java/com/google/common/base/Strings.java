package com.google.common.base;

public final class Strings {
    private Strings() {
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
