package com.github.stefanbirkner.fishbowl;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.github.stefanbirkner.fishbowl.Fishbowl.*;
import static java.lang.Long.parseLong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    private static class IgnoringExceptios {
        boolean somethingElseCalled = false;

        public void doSomething() {
            MyWorker worker = new MyWorker();
            ignoreException(() -> worker.doSomethingThatThrowsAnException());
            //the following statement is executed even if worker throws an
            //exception.
            doSomethingElse();
        }

        private void doSomethingElse() {
            somethingElseCalled = true;
        }

        @Test
        public void verifyThatTheMethodIsCompletelyExecuted() {
            doSomething();
            assertTrue(somethingElseCalled);
        }

        private static class MyWorker {
            public void doSomethingThatThrowsAnException() {
                throw new RuntimeException();
            }
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
