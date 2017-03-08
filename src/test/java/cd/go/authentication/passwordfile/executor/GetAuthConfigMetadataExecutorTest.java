package cd.go.authentication.passwordfile.executor;

import cd.go.authentication.passwordfile.annotation.MetadataHelper;
import cd.go.authentication.passwordfile.model.Configuration;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class GetAuthConfigMetadataExecutorTest {

    @Test
    public void shouldSerializeAllFields() throws Exception {
        GoPluginApiResponse response = new GetAuthConfigMetadataExecutor().execute();
        List list = new Gson().fromJson(response.responseBody(), List.class);
        assertEquals(list.size(), MetadataHelper.getMetadata(Configuration.class).size());
    }

    @Test
    public void assertJsonStructure() throws Exception {
        GoPluginApiResponse response = new GetAuthConfigMetadataExecutor().execute();

        assertThat(response.responseCode(), is(200));
        String expectedJSON = "[\n" +
                "  {\n" +
                "    \"key\": \"PasswordFilePath\",\n" +
                "    \"metadata\": {\n" +
                "      \"required\": true,\n" +
                "      \"secure\": false\n" +
                "    }\n" +
                "  }\n" +
                "]";

        JSONAssert.assertEquals(expectedJSON, response.responseBody(), true);
    }
}