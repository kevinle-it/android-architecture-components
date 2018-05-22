package com.example.trile.poc.utils;

/**
 * Utility for checking Null Object.
 *
 * @author trile
 * @since 5/22/18 at 14:19
 */
public class Objects {
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null)
            throw new NullPointerException(message);
        return obj;
    }

    public static boolean nonNull(Object obj) {
        return obj != null;
    }
}
