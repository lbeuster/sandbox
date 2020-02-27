package de.lbe.sandbox.protobuffers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.cartelsol.cate.shared.cl.client.NestedObject;
import com.cartelsol.cate.shared.cl.client.TestRequest;
import com.cartelsol.commons.lib.test.junit.AbstractJUnitTest;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.JsonFormat.Parser;
import com.google.protobuf.util.JsonFormat.Printer;

/**
 * @author lbeuster
 */
public class ProtoBuffersTest extends AbstractJUnitTest {

    @Test
    public void test_binary() throws InvalidProtocolBufferException {

        TestRequest.Builder builder = TestRequest.newBuilder();
        builder.setNested(NestedObject.newBuilder().setId(2).setName("hallo").build());
        TestRequest request = builder.build();
        byte[] bytes = request.toByteArray();

        TestRequest request2 = TestRequest.parseFrom(bytes);
        NestedObject nested1 = request.getNested();
        NestedObject nested2 = request2.getNested();
        assertEquals(nested1.getId(), nested2.getId());
        assertEquals(nested1.getName(), nested2.getName());
    }

    @Test
    public void test_json() throws InvalidProtocolBufferException {

        TestRequest.Builder builder = TestRequest.newBuilder();
        builder.setNested(NestedObject.newBuilder().setId(2).setName("hallo").build());
        TestRequest request = builder.build();

        Printer printer = JsonFormat.printer();
        String json = printer.print(request);
        System.out.println(json);

        Parser parser = JsonFormat.parser();
        builder = TestRequest.newBuilder();
        parser.merge(json, builder);
        TestRequest request2 = builder.build();

        NestedObject nested1 = request.getNested();
        NestedObject nested2 = request2.getNested();
        assertEquals(nested1.getId(), nested2.getId());
        assertEquals(nested1.getName(), nested2.getName());

    }
}
