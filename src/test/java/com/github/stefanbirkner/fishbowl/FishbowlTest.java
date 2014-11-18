package com.github.stefanbirkner.fishbowl;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.stefanbirkner.fishbowl.Fishbowl.exceptionThrownBy;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.rules.ExpectedException.none;

public class FishbowlTest {
    private static final Throwable DUMMY_EXCEPTION = new Exception();

    @Rule
    public final ExpectedException thrown = none().handleAssertionErrors();

    @Test
    public void exposesException() {
        Statement throwDummyException = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                throw DUMMY_EXCEPTION;
            }
        };
        Throwable exception = exceptionThrownBy(throwDummyException);
        assertThat(exception, is(sameInstance(DUMMY_EXCEPTION)));
    }

    @Test
    public void throwsExceptionIfStatementDidNotThrowOne() {
        Statement doNothing = new Statement() {
            @Override
            public void evaluate() throws Throwable {
            }
        };
        thrown.expect(ExceptionNotThrownFailure.class);
        exceptionThrownBy(doNothing);
    }
}
