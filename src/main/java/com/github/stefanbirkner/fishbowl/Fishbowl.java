package com.github.stefanbirkner.fishbowl;

/**
 * {@code Fishbowl} provides helper methods for dealing with exceptions.
 * <h2>Wrap Exceptions</h2>
 * <p>There are three options for dealing with checked exceptions that are
 * thrown inside of a method.
 * <ul>
 *     <li>Catch the exception and do some error handling.</li>
 *     <li>Add the exception's class to the throws part of the method's
 *     signature and don't handle it.</li>
 *     <li>Throw a {@code RuntimeException} that encloses the original exception.</li>
 * </ul>
 * <p>The methods {@link #wrapCheckedException(Statement)} (for
 * statements without a return value) and
 * {@link #wrapCheckedException(StatementWithReturnValue)} (for
 * statements with a return value) can be used for the third option.
 * They replace the try-catch-throw-RuntimeException snippet that is
 * usually used.
 * <h2>Default Values</h2>
 * <p>Sometimes exceptions are best handled by returning a default
 * value. This can be done by a single line of code with
 * {@link #defaultIfException(StatementWithReturnValue, Class, Object)}
 * <h2>Expose Exceptions</h2>
 * <p>Fishbowl can expose exceptions that are thrown by an arbitrary
 * piece of code. Thus you can write tests for that piece of code by
 * following the <a href="http://c2.com/cgi/wiki?ArrangeActAssert">AAA
 * (Arrange-Act-Assert) pattern</a>. Use
 * {@link #exceptionThrownBy(Statement)} for catching any
 * {@code Throwable} or use
 * {@link #exceptionThrownBy(Statement, Class)} for catching
 * exceptions of a specific type only.
 */
public class Fishbowl {
    /**
     * Executes the given statement and returns the statement's return
     * value if no exception is thrown or the default value if an
     * exception of the specified type is thrown.
     *
     * <pre>
     *   public void doSomething() {
     *     long value = defaultIfException(() -&gt; parseLong("NaN"), NumberFormatException.class, 0L);
     *     //value is 0
     *   }
     * </pre>
     *
     * <p>(Any other checked exception is wrapped just as it is wrapped by
     * {@link #wrapCheckedException(StatementWithReturnValue)}.)
     *
     * @param statement The statement that is executed.
     * @param exceptionType the type of exception for which the default
     *                      value is returned.
     * @param defaultValue this value is returned if the statement
     *                     throws an exception of the specified type.
     * @param <V> type of the value that is returned by the statement.
     * @return the return value of the statement or the default value.
     */
    public static <V> V defaultIfException(
        StatementWithReturnValue<V> statement,
        Class<? extends Throwable> exceptionType, V defaultValue) {
        try {
            return statement.evaluate();
        } catch (RuntimeException e) {
            if (exceptionType.isAssignableFrom(e.getClass()))
                return defaultValue;
            else
                throw e;
        } catch (Throwable e) {
            if (exceptionType.isAssignableFrom(e.getClass()))
                return defaultValue;
            else
                throw new WrappedException(e);
        }
    }

    /**
     * Executes the provided statement and returns the exception that
     * has been thrown by the statement. This is useful for writing
     * tests for exceptions according to the AAA (Arrange-Act-Assert)
     * pattern.
     * <p>The following test verifies that the statement
     * {@code noString.trim()} throws a {@code NullPointerException}.
     * <pre>
     * &#064;Test
     * public void anExceptionIsThrown() {
     *   String noString = null;
     *   Throwable exception = exceptionThrownBy}(() -&gt; noString.trim());
     *   assertEquals(NullPointerException.class, exception.getClass());
     * }
     * </pre>
     *
     * @param statement an arbitrary piece of code.
     * @return The exception thrown by the statement.
     * @throws ExceptionNotThrownFailure if the statement didn't throw
     * an exception.
     * @see #exceptionThrownBy(Statement, Class)
     */
    public static Throwable exceptionThrownBy(Statement statement) {
        return exceptionThrownBy(statement, Throwable.class);
    }

    /**
     * Executes the provided statement and returns the exception that
     * has been thrown by the statement if it has the specified type.
     * This is useful for writing tests for exceptions according to the
     * AAA (Arrange-Act-Assert) pattern in case that you need to check
     * properties of the exception.
     * <p>Example:
     * <pre>
     * FooException exception = exceptionThrownBy(
     *         () -&gt; { throw new FooException(3); }, FooException.class);
     * assertEquals(3, exception.getValue())
     * </pre>
     *
     * @param statement an arbitrary piece of code.
     * @param type the type of the exception that should be exposed.
     * @param <T> the type of the exception that should be exposed.
     * @return The exception thrown by the statement.
     * @throws ExceptionNotThrownFailure if the statement didn't throw
     * an exception.
     * @throws ExceptionWithWrongTypeThrownFailure if the statement
     * threw an exception of a different type.
     * @see #exceptionThrownBy(Statement)
     */
    public static <T extends Throwable> T exceptionThrownBy(
        Statement statement, Class<T> type) {
        try {
            statement.evaluate();
        } catch (Throwable e) {
            if (type.isAssignableFrom(e.getClass()))
                return (T) e;
            else
                throw new ExceptionWithWrongTypeThrownFailure(type, e);
        }
        throw new ExceptionNotThrownFailure();
    }

    /**
     * Executes the given statement and encloses any checked exception
     * thrown with an unchecked {@link WrappedException}, that
     * is thrown instead. The method
     * <pre>
     *   public void doSomething() {
     *     wrapCheckedException(() -&gt; throw new IOException());
     *   }
     * </pre>
     * <p>throws a {@code WrappedException} that encloses the
     * {@code IOException}.
     * <p>This avoids adding {@code throws IOException} to the method's
     * signature and is a replacement for the common
     * try-catch-throw-RuntimeException pattern:
     * <pre>
     *   public void doSomething() {
     *     try {
     *         throw new IOException();
     *     } catch (IOException e) {
     *         throw new RuntimeException(e);
     *     }
     *   }
     * </pre>
     *
     * @param statement The statement that is executed.
     * @see #wrapCheckedException(StatementWithReturnValue)
     */
    public static void wrapCheckedException(Statement statement) {
        try {
            statement.evaluate();
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new WrappedException(e);
        }
    }

    /**
     * Executes the given statement and encloses any checked exception
     * thrown with an unchecked {@link WrappedException}, that
     * is thrown instead. Returns the statement's return value if no
     * exception is thrown.
     * <pre>
     *   public void doSomething() {
     *     URL url = wrapCheckedException(() -&gt; new URL("http://something/"));
     *     ...
     *   }
     * </pre>
     * <p>This avoids adding {@code throws MalformedURLException} to the
     * method's signature and is a replacement for the common
     * try-catch-throw-RuntimeException pattern:
     * <pre>
     *   public void doSomething() {
     *     URL url;
     *     try {
     *         url = new URL("http://something/");
     *     } catch (MalformedURLException e) {
     *         throw new RuntimeException(e);
     *     }
     *     ...
     *   }
     * </pre>
     *
     * @param statement The statement that is executed.
     * @param <V> type of the value that is returned by the statement.
     * @return the return value of the statement.
     * @see #wrapCheckedException(Statement)
     */
    public static <V> V wrapCheckedException(StatementWithReturnValue<V> statement) {
        try {
            return statement.evaluate();
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new WrappedException(e);
        }
    }

    /**
     * This class only provides static methods. Hence nobody should
     * create {@code Fishbowl} objects.
     */
    private Fishbowl() {
    }
}
