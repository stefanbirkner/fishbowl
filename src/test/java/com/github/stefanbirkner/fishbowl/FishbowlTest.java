package com.github.stefanbirkner.fishbowl;


import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.io.IOException;

import static com.github.stefanbirkner.fishbowl.Fishbowl.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

    @Rule
    public final ExpectedException thrown = none();

    public class exceptionThrownBy_without_type {
        @Test
        public void returns_the_exception_that_is_thrown_by_the_provided_statement() {
            Throwable exception = exceptionThrownBy(
                statementThatThrows(DUMMY_EXCEPTION));
            assertThat(exception, is(sameInstance(DUMMY_EXCEPTION)));
        }

        @Test
        public void returns_an_ExceptionNotThrownFailure_if_the_provided_statement_did_not_throw_an_exception() {
            thrown.expect(ExceptionNotThrownFailure.class);
            exceptionThrownBy(DO_NOTHING);
        }
    }

    public class exceptionThrownBy_with_type {
        @Test
        public void returns_the_exception_that_is_thrown_by_the_provided_statement_if_it_has_the_expected_type() {
            Exception exception = exceptionThrownBy(
                statementThatThrows(DUMMY_EXCEPTION), Exception.class);
            assertThat(exception, is(sameInstance(DUMMY_EXCEPTION)));
        }

        @Test
        public void returns_the_exception_that_is_thrown_by_the_provided_statement_if_it_is_a_subtype_of_the_expected_type() {
            Throwable exception = exceptionThrownBy(
                statementThatThrows(DUMMY_EXCEPTION), Throwable.class);
            assertThat(exception, is(sameInstance(DUMMY_EXCEPTION)));
        }

        @Test
        public void throws_an_ExceptionNotThrownFailure_if_the_provided_statement_did_not_throw_an_exception() {
            thrown.expect(ExceptionNotThrownFailure.class);
            exceptionThrownBy(DO_NOTHING, Exception.class);
        }

        @Test
        public void throws_an_ExceptionWithWrongTypeThrownFailure_if_the_provided_statement_throws_an_exception_of_a_different_type() {
            thrown.expect(allOf(
                instanceOf(ExceptionWithWrongTypeThrownFailure.class),
                hasProperty("cause", sameInstance(DUMMY_EXCEPTION)),
                hasProperty("expectedType", equalTo(NullPointerException.class)),
                hasProperty("thrownException", sameInstance(DUMMY_EXCEPTION))
            ));
            exceptionThrownBy(
                statementThatThrows(DUMMY_EXCEPTION),
                NullPointerException.class);
        }
    }

    public class wrapCheckedException_for_statement_without_return_value {
        @Test
        public void throws_a_WrappedException_whose_cause_is_the_exception_that_is_thrown_by_the_provided_statement() {
            thrown.expect(WrappedException.class);
            thrown.expectCause(sameInstance(DUMMY_EXCEPTION));
            wrapCheckedException(statementThatThrows(DUMMY_EXCEPTION));
        }

        @Test
        public void throws_the_RuntimeException_that_is_thrown_by_the_provided_statement() {
            thrown.expect(sameInstance(DUMMY_RUNTIME_EXCEPTION));
            wrapCheckedException(new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    throw DUMMY_RUNTIME_EXCEPTION;
                }
            });
        }

        @Test
        public void throws_no_exception_if_the_provided_statement_throws_no_exception() {
            wrapCheckedException(DO_NOTHING);
        }
    }

    public class wrapCheckedException_for_statement_with_return_value {
        @Test
        public void throws_a_WrappedException_whose_cause_is_the_exception_that_is_thrown_by_the_provided_statement() {
            thrown.expect(WrappedException.class);
            thrown.expectCause(sameInstance(DUMMY_EXCEPTION));
            wrapCheckedException(
                statementWithReturnValueThatThrows(DUMMY_EXCEPTION));
        }

        @Test
        public void throws_the_RuntimeException_that_is_thrown_by_the_provided_statement() {
            thrown.expect(sameInstance(DUMMY_RUNTIME_EXCEPTION));
            wrapCheckedException(
                statementWithReturnValueThatThrows(DUMMY_RUNTIME_EXCEPTION));
        }

        @Test
        public void returns_the_return_value_of_the_provided_statement_if_it_throws_no_exception() {
            String value = wrapCheckedException(RETURN_EMPTY_STRING);
            assertThat(value, is(equalTo("")));
        }
    }

    public class defaultIfException {
        @Test
        public void returns_default_value_if_provided_statement_throws_RuntimeException_of_specified_type() {
            String value = defaultIfException(
                statementWithReturnValueThatThrows(new NumberFormatException()),
                NumberFormatException.class,
                "dummy value");
            assertThat(value, is(equalTo("dummy value")));
        }

        @Test
        public void returns_default_value_if_provided_statement_throws_checked_exception_of_specified_type() {
            String value = defaultIfException(
                statementWithReturnValueThatThrows(new IOException()),
                IOException.class,
                "dummy value");
            assertThat(value, is(equalTo("dummy value")));
        }

        @Test
        public void throws_a_WrappedException_whose_cause_is_the_exception_that_is_thrown_by_the_provided_statement_if_it_does_not_have_the_expected_type() {
            thrown.expect(WrappedException.class);
            thrown.expectCause(sameInstance(DUMMY_EXCEPTION));
            defaultIfException(
                statementWithReturnValueThatThrows(DUMMY_EXCEPTION),
                IOException.class,
                "");
        }

        @Test
        public void throws_the_RuntimeException_that_is_thrown_by_the_provided_statement_if_it_does_not_have_the_expected_type() {
            thrown.expect(sameInstance(DUMMY_RUNTIME_EXCEPTION));
            defaultIfException(
                statementWithReturnValueThatThrows(DUMMY_RUNTIME_EXCEPTION),
                IOException.class,
                "");
        }

        @Test
        public void returns_return_value_of_provided_statement_if_it_throws_no_exception() {
            String value = defaultIfException(RETURN_EMPTY_STRING, IOException.class, "default value");
            assertThat(value, is(equalTo("")));
        }
    }

    public class ignoreException_without_type {
        @Test
        public void suppresses_an_exception_that_is_thrown_by_the_provided_statement() {
            ignoreException(statementThatThrows(DUMMY_EXCEPTION));
        }

        @Test
        public void executes_the_provided_statement() throws Throwable {
            Statement statement = mock(Statement.class);
            ignoreException(statement);
            verify(statement).evaluate();
        }
    }

    public class ignoreException_with_type {
        @Test
        public void suppresses_a_RuntimeException_of_the_expected_type_that_is_thrown_by_the_provided_statement() {
            Statement statement = statementThatThrows(new IllegalArgumentException());
            ignoreException(statement, IllegalArgumentException.class);
        }

        @Test
        public void suppresses_a_checked_exception_of_the_expected_type_that_is_thrown_by_the_provided_statement() {
            Statement statement = statementThatThrows(new AssertionError());
            ignoreException(statement, AssertionError.class);
        }

        @Test
        public void throws_the_runtime_exception_that_is_thrown_by_the_provided_statement_if_a_different_type_is_expected() {
            Statement statement = statementThatThrows(new IllegalArgumentException());
            thrown.expect(IllegalArgumentException.class);
            ignoreException(statement, NullPointerException.class);
        }

        @Test
        public void throws_a_WrappedException_whose_cause_is_the_exception_that_is_thrown_by_the_provided_statement_if_it_does_not_have_the_expected_type() {
            thrown.expect(WrappedException.class);
            thrown.expectCause(sameInstance(DUMMY_EXCEPTION));
            ignoreException(
                statementThatThrows(DUMMY_EXCEPTION),
                NullPointerException.class);
        }

        @Test
        public void executes_the_provided_statement() throws Throwable {
            Statement statement = mock(Statement.class);
            ignoreException(statement, RuntimeException.class);
            verify(statement).evaluate();
        }
    }

    private static Statement statementThatThrows(final Throwable exception) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                throw exception;
            }
        };
    }

    private static StatementWithReturnValue<String> statementWithReturnValueThatThrows(
            final Throwable exception) {
        return new StatementWithReturnValue<String>() {
            @Override
            public String evaluate() throws Throwable {
                throw exception;
            }
        };
    }
}
