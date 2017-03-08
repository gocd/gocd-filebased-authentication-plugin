package cd.go.authentication.passwordfile.executor;

import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthConfigValidateRequestExecutorTest {

    private GoPluginApiRequest request;

    @Before
    public void setup() throws Exception {
        request = mock(GoPluginApiRequest.class);
    }

    @Test
    public void shouldBarfWhenUnknownKeysArePassed() throws Exception {
        when(request.requestBody()).thenReturn(new Gson().toJson(Collections.singletonMap("foo", "bar")));

        GoPluginApiResponse response = new AuthConfigValidateRequestExecutor(request).execute();
        String json = response.responseBody();

        String expectedJSON = "[\n" +
                "  {\n" +
                "    \"message\": \"PasswordFilePath must not be blank.\",\n" +
                "    \"key\": \"PasswordFilePath\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"foo\",\n" +
                "    \"message\": \"Is an unknown property\"\n" +
                "  }\n" +
                "]";
        JSONAssert.assertEquals(expectedJSON, json, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    public void shouldValidateMandatoryKeys() throws Exception {
        when(request.requestBody()).thenReturn(new Gson().toJson(Collections.emptyMap()));

        GoPluginApiResponse response = new AuthConfigValidateRequestExecutor(request).execute();
        String json = response.responseBody();

        String expectedJSON = "[\n" +
                "  {\n" +
                "    \"message\": \"PasswordFilePath must not be blank.\",\n" +
                "    \"key\": \"PasswordFilePath\"\n" +
                "  }\n" +
                "]";
        JSONAssert.assertEquals(expectedJSON, json, JSONCompareMode.NON_EXTENSIBLE);
    }
}