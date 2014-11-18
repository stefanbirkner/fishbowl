package com.github.stefanbirkner.fishbowl;

/**
 * This exception is thrown by
 * {@link com.github.stefanbirkner.fishbowl.Fishbowl#exceptionThrownBy(Statement)}
 * if the provided {@link com.github.stefanbirkner.fishbowl.Statement}
 * did not throw an exception. It extends
 * {@link java.lang.AssertionError} in order to trigger a test failure.
 */
public class ExceptionNotThrownFailure extends AssertionError {
    /**
     * Creates a new {@code ExceptionNotThrownFailure}.
     */
    public ExceptionNotThrownFailure() {
        super("The Statement did not throw an exception.");
    }
}
