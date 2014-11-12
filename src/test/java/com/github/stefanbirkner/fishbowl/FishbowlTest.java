package com.github.stefanbirkner.fishbowl;


import org.junit.Test;

import static com.github.stefanbirkner.fishbowl.Fishbowl.exceptionThrownBy;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class FishbowlTest {
    private static final Throwable DUMMY_EXCEPTION = new Exception();

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
}
