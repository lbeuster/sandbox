package de.lbe.sandbox.flatbuffers;

import java.nio.ByteBuffer;

import org.junit.Test;

import com.cartelsol.commons.lib.test.junit.AbstractJUnitTest;
import com.google.flatbuffers.FlatBufferBuilder;

/**
 * 
 */
public class FlatBuffersTest extends AbstractJUnitTest {

    @Test
    public void testServiceSpec() {

        FlatBufferBuilder builder = new FlatBufferBuilder();
        String id = "MyService";
        int idOffset = builder.createString(id);
        String name = "MY_SERVICE";
        int nameOffset = builder.createString(name);
        String version = "2.1";
        int versionOffset = builder.createString(version);
        String messageFormat = "Message/Format";
        int messageFormatOffset = builder.createString(messageFormat);
        String description = "This is my description";
        int descriptionOffset = builder.createString(description);

        String path1 = "/path1";
        String path2 = "/path2";
        int[] pathsOffsets = { builder.createString(path1), builder.createString(path2) };
        int pathsOffset = ServiceSpecification.createPathsVector(builder, pathsOffsets);
        int offset = ServiceSpecification.createServiceSpecification(builder, idOffset, nameOffset, versionOffset, pathsOffset, messageFormatOffset, descriptionOffset);

        builder.finish(offset);

        // java.nio.ByteBuffer buf = builder.dataBuffer();
        byte[] bytes = builder.sizedByteArray();

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

        // Get an accessor to the root object inside the buffer.
        ServiceSpecification spec = ServiceSpecification.getRootAsServiceSpecification(byteBuffer);

        assertEquals(id, spec.id());
        assertEquals(name, spec.name());
        assertEquals(version, spec.version());
        assertEquals(messageFormat, spec.messageFormat());
        assertEquals(description, spec.description());
        assertEquals(2, spec.pathsLength());
        assertEquals(path1, spec.paths(0));
        assertEquals(path2, spec.paths(1));
    }
}
