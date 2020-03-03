package de.lbe.sandbox.json;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;

import com.cartelsol.commons.lib.json.gson.Gson;
import com.cartelsol.commons.lib.test.junit.AbstractJUnitTest;

/**
 * @author lbeuster
 */
public class JsonSchemaValidationTest extends AbstractJUnitTest {

    @Test
    public void test() throws Exception {

        CarValue value = new CarValue();
        value.setName("KEY");
        value.setValue("VALUE");

        List<CarValue> values = new ArrayList<>();
        values.add(value);

        GetValuesResponse response = new GetValuesResponse();
        response.setCarValues(values);

        CarValuesService service = new CarValuesService();
        service.setGetValuesResponse(response);

        String json = Gson.toString(service);
        System.out.println(json);

        JSONObject rawSchema;
        try (InputStream inputStream = getClass().getResourceAsStream("/schema/CarValuesService.json")) {
            rawSchema = new JSONObject(new JSONTokener(inputStream));
        }
        // Schema schema = SchemaLoader.load(rawSchema);

        SchemaLoader loader = SchemaLoader.builder().schemaJson(rawSchema).draftV6Support().build();
        Schema schema = loader.load().build();

        json = "{\"getValuesResponse\":{\"carValues\":[{\"name1‚\":\"KEY\",\"value2\":\"VALUE\"}]}}";
        schema.validate(new JSONObject(json));

    }

    @Test
    public void test_geoPosition() throws Exception {

        GeoPosition pos = new GeoPosition();
        pos.setLatitude(1d);
        // pos.setLongitude(2d);
        String json = Gson.toString(pos);
        System.out.println(json);

        JSONObject rawSchema;
        try (InputStream inputStream = getClass().getResourceAsStream("/schema/GeoPosition.json")) {
            rawSchema = new JSONObject(new JSONTokener(inputStream));
        }
        // Schema schema = SchemaLoader.load(rawSchema);

        SchemaLoader loader = SchemaLoader.builder().schemaJson(rawSchema).draftV6Support().build();
        Schema schema = loader.load().build();

        // json = "{\"hello\" : \"world\"}";
        schema.validate(new JSONObject(json));

    }
}
