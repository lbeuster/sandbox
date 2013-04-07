package de.lbe.sandbox.java7;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 * Exceptions koennen jetzt beliebig viele suppressed Exceptions haben. Suppressed Exception haben nichts mit der
 * eigentlichen Exception zu tun, sondern treten im Windschatten dieser auf, gehoeren aber noch zur selben Situation.
 * Das Cause-Feld einer Exception (seit Java 1.4) ist weiterhin die eigentliche Ursache.
 */
public class _07SuppressedExceptionTest extends Assert {

    /**
     * This is the old behavior. An exception in the finally-clause hides the exception in the try clause.
     */
    @Test(expected = IOException.class)
    public final void testOldTryFinallyExceptionHandling()
        throws Exception {
        TestOutputStream out = new TestOutputStream();
        try {
            throw new IllegalStateException();
        } finally {
            out.close();
        }
    }

    /**
     * This is the new behavior (only for try-with-resource-statements). An exception in the "autoclose"-block is added
     * as a suppressed exception to the exception in the try-block.
     */
    @Test
    public final void testTryWithResourceExceptionHandling()
        throws Exception {
        try {
            // this try-with-resources throw a
            // * IllegalArgument in the try block and
            // * IOException in the autoclose-block
            // -> but the IOException is suppressed
            try (TestOutputStream out = new TestOutputStream();) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException ex) {
            // @NEW
            assertEquals(1, ex.getSuppressed().length);
            assertSame(IOException.class, ex.getSuppressed()[0].getClass());
        }
    }

    /**
     * We can programmatically add suppressed exceptions to any exception.
     */
    @Test
    public final void testAddSuppressedException() {
        Exception exception = new Exception();
        Exception suppressed = new Exception();
        assertEquals(0, exception.getSuppressed().length);
        // @NEW
        exception.addSuppressed(suppressed);
        assertEquals(1, exception.getSuppressed().length);
        assertSame(suppressed, exception.getSuppressed()[0]);
    }

    /**
     * 
     */
    private static final class TestOutputStream extends ByteArrayOutputStream {

        @Override
        public final void close()
            throws IOException {
            throw new IOException("close");
        }
    }

}
