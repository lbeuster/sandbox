package de.lbe.sandbox.java7;

import org.junit.Assert;
import org.junit.Test;

/**
 * Man kann jetzt String-Werte auch in switch-Anweisungen verwenden. Die verwendeten String muessen nur final sein.
 */
public class _05StringsInSwitchTest extends Assert {

    /**
     *
     */
    @Test
    public final void testSwitchOverFinalString() {
        final String CONSTANT = "hallo";
        String value = new String(CONSTANT);
        assertFalse(value == CONSTANT);
        // @NEW
        switch (value) {
            case CONSTANT:
                return;
        }
        fail("'" + value + "' not covered in switch");
    }

}
