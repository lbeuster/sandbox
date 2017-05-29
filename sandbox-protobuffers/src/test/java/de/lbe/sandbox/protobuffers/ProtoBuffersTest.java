package de.lbe.sandbox.protobuffers;

import org.junit.Test;

import com.cartelsol.commons.lib.test.junit.AbstractJUnitTest;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * 
 */
public class ProtoBuffersTest extends AbstractJUnitTest {

    @Test
    public void testServiceSpec() throws InvalidProtocolBufferException {

        String id = "MyService";
        String name = "MY_SERVICE";
        String version = "2.1";
        String messageFormat = "Message/Format";
        String description = "This is my description";
        String path1 = "/path1";
        String path2 = "/path2";

        ServiceSpecification.Builder builder = ServiceSpecification.newBuilder();
        builder.setId(id).setName(name).setVersion(version).setMessageFormat(messageFormat).setDescription(description);
        builder.addPaths(path1).addPaths(path2);
        ServiceSpecification spec = builder.build();
        byte[] bytes = spec.toByteArray();

        spec = ServiceSpecification.parseFrom(bytes);

        assertEquals(id, spec.getId());
        assertEquals(name, spec.getName());
        assertEquals(version, spec.getVersion());
        assertEquals(messageFormat, spec.getMessageFormat());
        assertEquals(description, spec.getDescription());
        assertEquals(2, spec.getPathsCount());
        assertEquals(path1, spec.getPaths(0));
        assertEquals(path2, spec.getPaths(1));
    }
}
