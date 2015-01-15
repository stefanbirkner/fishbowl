package com.github.stefanbirkner.fishbowl;

/**
 * This exception is thrown by
 * {@link com.github.stefanbirkner.fishbowl.Fishbowl#exceptionThrownBy(Statement, java.lang.Class)}
 * if the provided {@link com.github.stefanbirkner.fishbowl.Statement}
 * did throw an exception that does not have the specified type. It
 * extends {@link java.lang.AssertionError} in order to trigger a test
 * failure.
 */
public class ExceptionWithWrongTypeThrownFailure extends AssertionError {
    private final Class<? extends Throwable> expectedType;

    /**
     * Creates a new {@code ExceptionWithWrongTypeThrownFailure}.
     * @param expectedType The type of the expected exception.
     * @param thrownException the exception that has been thrown.
     */
    public ExceptionWithWrongTypeThrownFailure(
        Class<? extends Throwable> expectedType, Throwable thrownException) {
        super("The Statement threw a " + thrownException.getClass().getName()
                + " instead of a " + expectedType.getName() + ".");
        initCause(thrownException);
        this.expectedType = expectedType;
    }

    /**
     * Returns the type of the expected exception.
     * @return the type of the expected exception.
     */
    public Class<? extends Throwable> getExpectedType() {
        return expectedType;
    }

    /**
     * Returns the exception that has been thrown.
     * @return the exception that has been thrown.
     */
    public Throwable getThrownException() {
        return getCause();
    }
}
