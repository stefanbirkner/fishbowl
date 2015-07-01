package com.github.stefanbirkner.fishbowl;

/**
 * Wraps another exception. This is used for wrapping non
 * RuntimeException in order to allow methods to have a signature without
 * throws.
 */
public class WrappedException extends RuntimeException {
    public WrappedException(Throwable cause) {
        super("See original cause.", cause);
    }
}
