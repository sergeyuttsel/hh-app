package hh.util;

import com.google.gson.Gson;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;

public class JsonUtil {
    public Configuration createJsonPathConfiguration() {
        return new Configuration.ConfigurationBuilder()
                .jsonProvider(new GsonJsonProvider())
                .mappingProvider(new GsonMappingProvider())
                .build();
    }

    public String getValueFromJson(String jsonPath, String json) {
        try {
            Gson gsonObject = new Gson();
            ReadContext ctx = JsonPath.parse(json, createJsonPathConfiguration());
            return gsonObject.toJsonTree(ctx.read(jsonPath)).getAsString();
        } catch (JsonPathException | UnsupportedOperationException e) {
            return null;
        }

    }
}
