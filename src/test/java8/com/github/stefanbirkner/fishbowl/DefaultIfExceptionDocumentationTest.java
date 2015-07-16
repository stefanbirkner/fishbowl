package com.github.stefanbirkner.fishbowl;

import org.junit.Test;

import static com.github.stefanbirkner.fishbowl.Fishbowl.defaultIfException;
import static java.lang.Long.parseLong;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Test the Javadoc code for
 * {@link Fishbowl#defaultIfException(StatementWithReturnValue, Class, Object)}.
 */
public class DefaultIfExceptionDocumentationTest {
    @Test
    public void returnsDefaultValueIfExceptionOfSpecifiedTypeIsThrown() {
        long value = defaultIfException(() -> parseLong("NaN"), NumberFormatException.class, 0L);
        assertThat(value, is(0L));
    }
}
