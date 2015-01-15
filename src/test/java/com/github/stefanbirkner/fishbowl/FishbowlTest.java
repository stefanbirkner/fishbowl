package com.github.stefanbirkner.fishbowl;


import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static com.github.stefanbirkner.fishbowl.Fishbowl.exceptionThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.rules.ExpectedException.none;

@RunWith(HierarchicalContextRunner.class)
public class FishbowlTest {
    private static final Throwable DUMMY_EXCEPTION = new Exception();
    private static final Statement DO_NOTHING = new Statement() {
        @Override
        public void evaluate() throws Throwable {
        }
    };
    private static final Statement THROW_DUMMY_EXCEPTION = new Statement() {
        @Override
        public void evaluate() throws Throwable {
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
}
