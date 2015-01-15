package com.github.stefanbirkner.fishbowl;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;

public class ExceptionWithWrongTypeThrownFailureTest {
    @Test
    public void hasNiceMessage() {
        Throwable throwable = new ExceptionWithWrongTypeThrownFailure(IllegalArgumentException.class, new NullPointerException());
        assertThat(throwable, hasProperty("message", equalTo(
            "The Statement threw a java.lang.NullPointerException instead of a java.lang.IllegalArgumentException.")));
    }
}
