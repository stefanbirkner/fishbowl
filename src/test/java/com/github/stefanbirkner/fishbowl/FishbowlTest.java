package com.github.stefanbirkner.fishbowl;


import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static com.github.stefanbirkner.fishbowl.Fishbowl.exceptionThrownBy;
import static com.github.stefanbirkner.fishbowl.Fishbowl.wrapCheckedException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.rules.ExpectedException.none;

@RunWith(HierarchicalContextRunner.class)
public class FishbowlTest {
    private static final Throwable DUMMY_EXCEPTION = new Exception();
    private static final RuntimeException DUMMY_RUNTIME_EXCEPTION = new RuntimeException();
    private static final Statement DO_NOTHING = new Statement() {
        @Override
        public void evaluate() throws Throwable {
        }
    };
    private static final StatementWithReturnValue<String> RETURN_EMPTY_STRING = new StatementWithReturnValue<String>() {
        @Override
        public String evaluate() throws Throwable {
            return "";
        }
    };
    private static final Statement THROW_DUMMY_EXCEPTION = new Statement() {
        @Override
        public void evaluate() throws Throwable {
            throw DUMMY_EXCEPTION;
        }
    };
    private static final StatementWithReturnValue<String> THROW_DUMMY_EXCEPTION_2 = new StatementWithReturnValue<String>() {
        @Override
        public String evaluate() throws Throwable {
            throw DUMMY_EXCEPTION;
        }
    };

    @Rule
    public final ExpectedException thrown = none();

    public class ExceptionThrownByForArbitraryException {
        @Test
        public void exposesException() {
            Throwable exception = exceptionThrownBy(THROW_DUMMY_EXCEPTION);
            assertThat(exception, is(sameInstance(DUMMY_EXCEPTION)));
        }

        @Test
        public void throwsExceptionIfStatementDidNotThrowOne() {
            thrown.expect(ExceptionNotThrownFailure.class);
            exceptionThrownBy(DO_NOTHING);
        }
    }

    public class ExceptionThrownByForSpecifiedTypeOfException {
        @Test
        public void exposesExceptionOfCorrectType() {
            Exception exception = exceptionThrownBy(THROW_DUMMY_EXCEPTION, Exception.class);
            assertThat(exception, is(sameInstance(DUMMY_EXCEPTION)));
        }

        @Test
        public void exposesExceptionOfSubType() {
            Throwable exception = exceptionThrownBy(THROW_DUMMY_EXCEPTION, Throwable.class);
            assertThat(exception, is(sameInstance(DUMMY_EXCEPTION)));
        }

        @Test
        public void throwsExceptionIfStatementDidNotThrowOne() {
            thrown.expect(ExceptionNotThrownFailure.class);
            exceptionThrownBy(DO_NOTHING, Exception.class);
        }

        @Test
        public void throwsExceptionIfStatementThrowsOneWithWrongType() {
            thrown.expect(allOf(
                instanceOf(ExceptionWithWrongTypeThrownFailure.class),
                hasProperty("cause", sameInstance(DUMMY_EXCEPTION)),
                hasProperty("expectedType", equalTo(NullPointerException.class)),
                hasProperty("thrownException", sameInstance(DUMMY_EXCEPTION))
            ));
            exceptionThrownBy(THROW_DUMMY_EXCEPTION, NullPointerException.class);
        }
    }

    public class WrapCheckedException {
        @Test
        public void wrapsCheckedExceptionThrownByStatement() {
            thrown.expect(WrappedException.class);
            thrown.expectCause(sameInstance(DUMMY_EXCEPTION));
            wrapCheckedException(THROW_DUMMY_EXCEPTION);
        }

        @Test
        public void doesNotWrapRuntimeExceptionThrownByStatement() {
            thrown.expect(sameInstance(DUMMY_RUNTIME_EXCEPTION));
            wrapCheckedException(new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    throw DUMMY_RUNTIME_EXCEPTION;
                }
            });
        }

        @Test
        public void doesNotInterceptStatementThatDoesNotThrowAnException() {
            wrapCheckedException(DO_NOTHING);
        }

        @Test
        public void wrapsCheckedExceptionThrownByStatementWithReturnValue() {
            thrown.expect(WrappedException.class);
            thrown.expectCause(sameInstance(DUMMY_EXCEPTION));
            wrapCheckedException(THROW_DUMMY_EXCEPTION_2);
        }

        @Test
        public void doesNotWrapRuntimeExceptionThrownByStatementWithReturnValue() {
            thrown.expect(sameInstance(DUMMY_RUNTIME_EXCEPTION));
            wrapCheckedException(new StatementWithReturnValue<String>() {
                @Override
                public String evaluate() throws Throwable {
                    throw DUMMY_RUNTIME_EXCEPTION;
                }
            });
        }

        @Test
        public void provideReturnValueIfNoExceptionIsThrown() {
            String value = wrapCheckedException(RETURN_EMPTY_STRING);
            assertThat(value, is(equalTo("")));
        }
    }
}
