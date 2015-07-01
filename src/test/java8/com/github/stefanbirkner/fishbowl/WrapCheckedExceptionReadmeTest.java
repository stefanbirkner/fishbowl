package com.github.stefanbirkner.fishbowl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.URL;

import static com.github.stefanbirkner.fishbowl.Fishbowl.wrapCheckedException;

/**
 * Test the Readme's examples for wrapException.
 */
public class WrapCheckedExceptionReadmeTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void wrapsCodeWithoutReturnValue() {
        thrown.expect(RuntimeException.class);
        wrapCheckedException(() -> { throw new IOException(); });
    }

    @Test
    public void wrapsCodeWithReturnValue() {
        URL url = wrapCheckedException(() -> new URL("http://junit.org/"));
    }
}
