package de.lbe.sandbox.java7;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.junit.Assert;
import org.junit.Test;

import de.asideas.lib.commons.io.IOUtils;

/**
 * Es gibt ein neues try-Konstrukt, mit dem Resourcen, die im try-Header geoeffnet werden, automatisch beim Verlassen
 * des Try-Statements wieder geschlossen werden.
 */
public class _06TryWithResourceTest extends Assert {

    /**
     *
     */
    @Test
    public final void testAutoClose()
        throws Exception {

        final MutableBoolean closeCalled = new MutableBoolean(false);
        assertFalse(closeCalled.booleanValue());
        assertTrue(AutoCloseable.class.isAssignableFrom(TestInputStream.class));

        // read the input stream
        // @NEW
        try (TestInputStream in = new TestInputStream(closeCalled)) {
            IOUtils.toByteArray(in);
        }

        // assert that the close method is called automatically
        assertTrue(closeCalled.booleanValue());
    }

    /**
     *
     */
    @Test
    public final void testAutoCloseTwoResources()
        throws Exception {

        final MutableBoolean close1Called = new MutableBoolean(false);
        final MutableBoolean close2Called = new MutableBoolean(false);

        // read the input streams
        // @NEW
        try (TestInputStream in1 = new TestInputStream(close1Called);
                TestInputStream in2 = new TestInputStream(close2Called)) {
            IOUtils.toByteArray(in1);
            IOUtils.toByteArray(in2);
        }

        // assert that the close methods are called automatically
        assertTrue(close1Called.booleanValue());
        assertTrue(close2Called.booleanValue());
    }

    /**
     *
     */
    private static final class TestInputStream extends ByteArrayInputStream {

        private final MutableBoolean closeCalled;

        TestInputStream(MutableBoolean closeCalled) {
            super(new byte[10]);
            this.closeCalled = closeCalled;
        }

        @Override
        public final void close()
            throws IOException {
            this.closeCalled.setValue(true);
            super.close();
        }
    }

}
