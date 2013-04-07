package de.lbe.sandbox.java7;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.zanox.lib.commons.io.IOUtils;

/**
 * 
 */
public class _03ImprovedRethrowExceptionTest extends Assert {

    /**
     * 
     */
    @Test(expected = FileNotFoundException.class)
    public final void testRethrow()
        throws FileNotFoundException {
    	InputStream in = null;
        try {
            in = new FileInputStream(new File("invalid"));
            fail("non-existing file " + in);
        } catch (final Throwable ex) {
            // @NEW
            // wir koennen Throwable abfangen und weiterwerfen - die deklarierte Exception unserer Methode kann ruhig
            // FileNotFoundException bleiben. Der Compiler geht davon aus, dass unsere Methode nur Exceptions werfen
            // kann, die auch im try-Block geworfen werden koennen
            throw ex;
        } finally {
        	IOUtils.closeQuietly(in);
        }
    }

}
