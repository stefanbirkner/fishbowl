package com.github.stefanbirkner.fishbowl;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.github.stefanbirkner.fishbowl.Fishbowl.defaultIfException;
import static com.github.stefanbirkner.fishbowl.Fishbowl.exceptionThrownBy;
import static com.github.stefanbirkner.fishbowl.Fishbowl.wrapCheckedException;
import static java.lang.Long.parseLong;
import static org.junit.Assert.assertEquals;

/**
 * Test that the website's example code is working.
 */
@RunWith(Enclosed.class)
public class WebsiteExampleTest {
    private static class WrappingExceptions {
        public void doSomething(InputStream is) {
            //originally both lines can throw an IOException
            int nextByte = wrapCheckedException(() -> is.read());
            wrapCheckedException(() -> is.close());
        }

        @Test
        public void verifyThatTheMethodCanBeExecuted() {
            InputStream is = new ByteArrayInputStream("dummy".getBytes());
            doSomething(is);
        }
    }

    private static class DefaultValues {
        @Test
        public void doSomething() {
            long value = defaultIfException(() -> parseLong("NaN"),
                NumberFormatException.class, 0L);
            //value is 0
            assertEquals(0, value);
        }
    }

    private static class ExceptionTesting {
        @Test
        public void anExceptionIsThrown() {
            String noString = null;
            Throwable exception = exceptionThrownBy(() -> noString.trim());
            assertEquals(NullPointerException.class, exception.getClass());
        }
    }
}
