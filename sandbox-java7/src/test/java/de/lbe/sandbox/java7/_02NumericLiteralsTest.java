package de.lbe.sandbox.java7;

import org.junit.Assert;
import org.junit.Test;

/**
 * Man kann jetzt Underscores ('_') in allen numerischen Literalen verwenden, um die Lesbarkeit zu erhoehen.
 */
public class _02NumericLiteralsTest extends Assert {

    /**
     *
     */
    @Test
    public final void testLongWithUnderscore() {
        long longValue1 = 1234567890L;
        // @NEW
        long longValue2 = 1234_5__678_90L;
        assertEquals(longValue1, longValue2);
    }

    /**
     * 
     */
    @Test
    public final void testHexWithUnderscore() {
        long hexValue1 = 0xFFECDE5E;
        // @NEW
        long hexValue2 = 0xFFEC_DE5E;
        assertEquals(hexValue1, hexValue2);
    }

    /**
     * 
     */
    @Test
    public final void testBinaryWithUnderscore() {
        int decimalValue = 13;
        // @NEW
        int binaryValue1 = 0b11_01;
        assertEquals(decimalValue, binaryValue1);
    }
}
