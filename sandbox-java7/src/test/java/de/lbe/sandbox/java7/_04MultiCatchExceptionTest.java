package de.lbe.sandbox.java7;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

/**
 * Wir koennen jetzt mehrere Exceptions in einem einzigen Catch-Statement abfangen. Damit muss man Code nicht mehr
 * duplizieren, den man evtl. in beiden Catch-Statements ausgefuehrt haette.
 */
public class _04MultiCatchExceptionTest extends Assert {

    /**
     * 
     */
    @Test(expected = IOException.class)
    public final void testMultiCatch()
        throws IOException {
        try {
            Object unused = new FileInputStream(new File("invalid"));
            unused = new URL("invalid");
            assertNotNull(unused);
        } catch (final FileNotFoundException | MalformedURLException ex) { // @NEW
            throw ex;
        }
    }

}
