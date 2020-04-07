package de.lbe.sandbox.protobuf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.cartelsol.commons.lib.test.junit.AbstractJUnitTest;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.JsonFormat.Parser;
import com.google.protobuf.util.JsonFormat.Printer;

import de.lbe.sandbox.protobuf.any.ErrorStatus;
import de.lbe.sandbox.protobuf.test.NestedObject;
import de.lbe.sandbox.protobuf.test.TestPerson;
import de.lbe.sandbox.protobuf.test.TestRequest;

/**
 * @author lbeuster
 */
public class ProtoBufTest extends AbstractJUnitTest {

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

    @Test
    public void test_required_json() throws InvalidProtocolBufferException {

        String invalid = JsonFormat.printer().print(TestPerson.newBuilder().build());
        System.out.println(invalid);

        Parser parser = JsonFormat.parser();
        TestPerson.Builder builder = TestPerson.newBuilder();
        parser.merge(invalid, builder);
        TestPerson person = builder.build();

        assertEquals("", person.getName());
        assertFalse(person.hasField(TestPerson.getDescriptor().findFieldByNumber(TestPerson.NAME_FIELD_NUMBER)));
        assertEquals(0, person.getAge());
        assertFalse(person.hasField(TestPerson.getDescriptor().findFieldByNumber(TestPerson.AGE_FIELD_NUMBER)));
    }

    @Test
    public void test_required_binary() throws InvalidProtocolBufferException {

        byte[] bytes = TestPerson.newBuilder().build().toByteArray();
        TestPerson person = TestPerson.parseFrom(bytes);

        assertEquals("", person.getName());
        assertFalse(person.hasField(TestPerson.getDescriptor().findFieldByNumber(TestPerson.NAME_FIELD_NUMBER)));
        assertEquals(0, person.getAge());
        assertFalse(person.hasField(TestPerson.getDescriptor().findFieldByNumber(TestPerson.AGE_FIELD_NUMBER)));
    }

    @Test
    void test_any() throws Exception {

        TestPerson.Builder person = TestPerson.newBuilder().setName("TestName");

        Any any = Any.pack(person.build());
        ErrorStatus status = ErrorStatus.newBuilder().setMessage("Test").setDetails(any).build();

        byte[] bytes = status.toByteArray();
        System.out.println(new String(bytes, StandardCharsets.US_ASCII));
        ErrorStatus clone = ErrorStatus.parseFrom(bytes);
        Any anyClone = clone.getDetails();
        TestPerson personClone = anyClone.unpack(TestPerson.class);
        assertEquals(person.getName(), personClone.getName());
    }
}
