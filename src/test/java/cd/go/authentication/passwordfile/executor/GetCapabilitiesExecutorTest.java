package cd.go.authentication.passwordfile.executor;

import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertThat;

public class GetCapabilitiesExecutorTest {

    @Test
    public void shouldExposeItsCapabilities() throws Exception {
        GoPluginApiResponse response = new GetCapabilitiesExecutor().execute();

        assertThat(response.responseCode(), CoreMatchers.is(200));

        String expectedJSON = "{\n" +
                "    \"supported_auth_type\":\"password\",\n" +
                "    \"can_search\":true,\n" +
                "    \"can_verify_connection\":false,\n" +
                "    \"can_authorize\":false\n" +
                "}";

        JSONAssert.assertEquals(expectedJSON, response.responseBody(), true);
    }
}
