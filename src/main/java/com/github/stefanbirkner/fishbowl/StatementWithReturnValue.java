package com.github.stefanbirkner.fishbowl;

/**
 * Code that returns a value if it is executed by
 * {@link com.github.stefanbirkner.fishbowl.Fishbowl#exceptionThrownBy(Statement)}.
 * This code may throw a {@code Throwable}. Therefore we cannot use
 * {@code Callable}.
 */
public interface StatementWithReturnValue<V> {
    /**
     * Computes a value, or throws an exception if unable to do so.
     *
     * @return computed value
     * @throws Throwable an exception if it cannot compute the value
     */
    V evaluate() throws Throwable;
}
