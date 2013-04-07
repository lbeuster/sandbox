package de.lbe.sandbox.java7;

import org.junit.Assert;
import org.junit.Test;

/**
 * Bisher konnte man ja numerische Werte in Hexadezimal- und Oktal-Schreibweise ausdruecken. Jetzt kann man zusaetzlich
 * auch eine Binaer-Darstellung angeben.
 */
public class _01NumericBinarySyntaxTest extends Assert {

    /**
     *
     */
    @Test
    public final void testBinarySyntax() {
        int decimalValue = 13;
        // @NEW
        int binaryValue1 = 0b1101;
        assertEquals(decimalValue, binaryValue1);
    }
}
