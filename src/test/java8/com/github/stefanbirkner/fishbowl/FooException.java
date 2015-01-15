package com.github.stefanbirkner.fishbowl;

/**
 * Needed by {@link com.github.stefanbirkner.fishbowl.FishbowlJUnitReadmeTest#expectExceptionOfACertainType()}
 * and {@link com.github.stefanbirkner.fishbowl.FishbowlTestNgReadmeTest#expectExceptionOfACertainType()}
 */
public class FooException extends Exception {
    private final int value;

    public FooException(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
