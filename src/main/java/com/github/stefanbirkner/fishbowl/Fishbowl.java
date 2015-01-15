package com.github.stefanbirkner.fishbowl;

/**
 * Exposes exceptions that are thrown by an arbitrary piece of code.
 * Thus you can write tests for that piece of code by following the
 * <a href="http://c2.com/cgi/wiki?ArrangeActAssert">AAA
 * (Arrange-Act-Assert) pattern</a>.
 * <p>The following test verifies that the statement
 * {@code noString.trim()} throws a {@code NullPointerException}.
 * <pre>
 * &#064;Test
 * public void anExceptionIsThrown() {
 *   String noString = null;
 *   Throwable exception = {@link #exceptionThrownBy(Statement) exceptionThrownBy}(() -&gt; noString.trim());
 *   assertEquals(NullPointerException.class, exception.getClass());
 * }
 * </pre>
 * Fishbowl exposes the exception that is thrown by the piece of code
 * that has been provided to {@code exceptionThrownBy}. This exception
 * can be checked by any assertion library. (The example uses JUnit's
 * {@code Assert} class.)
 * <p>In case that the statement did not thrown an exception, Fishbowl
 * itself throws an {@link ExceptionNotThrownFailure}. This causes the
 * test to fail
 * <pre>
 * com.github.stefanbirkner.fishbowl.ExceptionNotThrownFailure: The Statement did not throw an exception.
 * </pre>
 * <p>If you need the exception to have a certain type then you can
 * call {@code exceptionThrownBy} with this type as second argument.
 * (E.g. for verifying the state of custom exceptions.)
 * <pre>
 * FooException exception = {@link #exceptionThrownBy(Statement, java.lang.Class) exceptionThrownBy}(
 *         () -&gt; { throw new FooException(3); }, FooException.class);
 * assertEquals(3, exception.getValue())
 * </pre>
 * <p>In case that the statement threw an exception of a different
 * type, Fishbowl itself throws an
 * {@code ExceptionWithWrongTypeThrownFailure}. This causes the test to
 * fail:
 * <pre>
 * com.github.stefanbirkner.fishbowl.ExceptionWithWrongTypeThrownFailure: The Statement threw an FooException instead of an java.lang.NullPointerException.
 *     ...
 * Caused by: FooException
 *     ...
 * </pre>
 * <h3>Example for Several Assertion Libraries</h3>
 * The example above uses JUnit's {@code Assert} class. Below is the
 * same test with other assertion libraries.
 *
 * <h4>Hamcrest</h4>
 * <pre>
 * &#064;Test
 * public void anExceptionIsThrown() {
 *   String noString = null;
 *   Throwable exception = exceptionThrownBy(() -&gt; noString.trim());
 *   assertThat(exception, instanceOf(NullPointerException.class));
 * }
 * </pre>
 *
 * <h4>AssertJ, FEST, Truth</h4>
 * <p>The test looks the same for all three assertion libraries. The
 * only difference is the class that provides {@code assertThat}.
 * <pre>
 * &#064;Test
 * public void anExceptionIsThrown() {
 *   String noString = null;
 *   Throwable exception = exceptionThrownBy(() -&gt; noString.trim());
 *   assertThat(exception).isInstanceOf(NullPointerException.class);
 * }
 * </pre>
 * For AssertJ please have a look at
 * <a href="http://joel-costigliola.github.io/assertj/core/api/org/assertj/core/api/AbstractThrowableAssert.html">AbstractThrowableAssert</a>
 * for further asserts.
 *
 * <h3>Java 6 and 7</h3>
 * <p>Fishbowl has been created with Java 8 in mind, but it can be used
 * with Java 6 and 7, too. In this case you have to use anonymous
 * classes. Here is the example from above for Java 6 and 7.
 * <pre>
 * &#064;Test
 * public void anExceptionIsThrown() {
 *   final String noString = null;
 *   Throwable exception = exceptionThrownBy(new Statement() {
 *     public void evaluate() throws Throwable {
 *       noString.trim();
 *     }
 *   }
 *   assertEquals(NullPointerException.class, exception.getClass());
 * }
 * </pre>
 */
public class Fishbowl {
    /**
     * Executes the provided statement and returns the exception that
     * has been thrown by the statement.
     *
     * @param statement an arbitrary piece of code.
     * @return The exception thrown by the statement.
     * @throws ExceptionNotThrownFailure if the statement didn't throw
     * an exception.
     */
    public static Throwable exceptionThrownBy(Statement statement) {
        return exceptionThrownBy(statement, Throwable.class);
    }

    /**
     * Executes the provided statement and returns the exception that
     * has been thrown by the statement if it has the specified type.
     *
     * @param statement an arbitrary piece of code.
     * @param type the type of the exception that should be exposed.
     * @param <T> the type of the exception that should be exposed.
     * @return The exception thrown by the statement.
     * @throws ExceptionNotThrownFailure if the statement didn't throw
     * an exception.
     * @throws ExceptionWithWrongTypeThrownFailure if the statement
     * threw an exception of a different type.
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
     * This class only provides static methods. Hence nobody should
     * create {@code Fishbowl} objects.
     */
    private Fishbowl() {
    }
}
