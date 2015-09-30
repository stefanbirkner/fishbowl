package com.github.stefanbirkner.fishbowl;


import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.io.IOException;

import static com.github.stefanbirkner.fishbowl.Fishbowl.defaultIfException;
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

    public class exceptionThrownBy_without_exception_type {
        @Test
        public void returns_the_exception_that_is_thrown_by_the_provided_statement() {
            Throwable exception = exceptionThrownBy(THROW_DUMMY_EXCEPTION);
            assertThat(exception, is(sameInstance(DUMMY_EXCEPTION)));
        }

        @Test
        public void returns_an_ExceptionNotThrownFailure_if_the_provided_statement_did_not_throw_an_exception() {
            thrown.expect(ExceptionNotThrownFailure.class);
            exceptionThrownBy(DO_NOTHING);
        }
    }

    public class exceptionThrownBy_with_exception_type {
        @Test
        public void returns_the_exception_that_is_thrown_by_the_provided_statement_if_it_has_the_expected_type() {
            Exception exception = exceptionThrownBy(THROW_DUMMY_EXCEPTION, Exception.class);
            assertThat(exception, is(sameInstance(DUMMY_EXCEPTION)));
        }

        @Test
        public void returns_the_exception_that_is_thrown_by_the_provided_statement_if_it_is_a_subtype_of_the_expected_type() {
            Throwable exception = exceptionThrownBy(THROW_DUMMY_EXCEPTION, Throwable.class);
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
            exceptionThrownBy(THROW_DUMMY_EXCEPTION, NullPointerException.class);
        }
    }

    public class wrapCheckedException_for_statement_without_return_value {
        @Test
        public void throws_a_WrappedException_whose_cause_is_the_exception_that_is_thrown_by_the_provided_statement() {
            thrown.expect(WrappedException.class);
            thrown.expectCause(sameInstance(DUMMY_EXCEPTION));
            wrapCheckedException(THROW_DUMMY_EXCEPTION);
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
            wrapCheckedException(THROW_DUMMY_EXCEPTION_2);
        }

        @Test
        public void throws_the_RuntimeException_that_is_thrown_by_the_provided_statement() {
            thrown.expect(sameInstance(DUMMY_RUNTIME_EXCEPTION));
            wrapCheckedException(new StatementWithReturnValue<String>() {
                @Override
                public String evaluate() throws Throwable {
                    throw DUMMY_RUNTIME_EXCEPTION;
                }
            });
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
            String value = defaultIfException(new StatementWithReturnValue<String>() {
                @Override
                public String evaluate() throws Throwable {
                    throw new NumberFormatException();
                }
            }, NumberFormatException.class, "dummy value");
            assertThat(value, is(equalTo("dummy value")));
        }

        @Test
        public void returns_default_value_if_provided_statement_throws_checked_exception_of_specified_type() {
            String value = defaultIfException(new StatementWithReturnValue<String>() {
                @Override
                public String evaluate() throws Throwable {
                    throw new IOException();
                }
            }, IOException.class, "dummy value");
            assertThat(value, is(equalTo("dummy value")));
        }

        @Test
        public void throws_a_WrappedException_whose_cause_is_the_exception_that_is_thrown_by_the_provided_statement_if_it_does_not_have_the_expected_type() {
            thrown.expect(WrappedException.class);
            thrown.expectCause(sameInstance(DUMMY_EXCEPTION));
            defaultIfException(THROW_DUMMY_EXCEPTION_2, IOException.class, "");
        }

        @Test
        public void throws_the_RuntimeException_that_is_thrown_by_the_provided_statement_if_it_does_not_have_the_expected_type() {
            thrown.expect(sameInstance(DUMMY_RUNTIME_EXCEPTION));
            defaultIfException(new StatementWithReturnValue<String>() {
                @Override
                public String evaluate() throws Throwable {
                    throw DUMMY_RUNTIME_EXCEPTION;
                }
            }, IOException.class, "");
        }

        @Test
        public void returns_return_value_of_provided_statement_if_it_throws_no_exception() {
            String value = defaultIfException(RETURN_EMPTY_STRING, IOException.class, "default value");
            assertThat(value, is(equalTo("")));
        }
    }
}
