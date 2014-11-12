package com.github.stefanbirkner.fishbowl;


/**
 * Code that should be executed by
 * {@link com.github.stefanbirkner.fishbowl.Fishbowl#exceptionThrownBy(Statement)}.
 * This code may throw a {@link Throwable}. Therefore we cannot use
 * {@link Runnable}.
 */
public interface Statement {
    /**
     * Execute the statement.
     *
     * @throws Throwable the statement may throw an arbitrary exception.
     */
    void evaluate() throws java.lang.Throwable;
}
