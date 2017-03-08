package cd.go.authentication.passwordfile.executor;

import cd.go.authentication.passwordfile.PasswordFileReader;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchUserExecutorTest {

    private PasswordFileReader fileReader;

    @Before
    public void setUp() throws Exception {
        fileReader = mock(PasswordFileReader.class);
    }

    @Test
    public void shouldListUsersMatchingTheSearchTerm() throws Exception {
        final GoPluginApiRequest request = mock(GoPluginApiRequest.class);

        final Properties properties = new Properties();
        properties.put("username", "invalid-password");
        when(fileReader.read("/var/etc/password.properties")).thenReturn(properties);
        when(request.requestBody()).thenReturn(requestJson("name"));

        final GoPluginApiResponse response = new SearchUserExecutor(request, fileReader).execute();

        assertThat(response.responseCode(), is(200));

        JSONAssert.assertEquals("[{\"username\":\"username\"}]", response.responseBody(), true);
    }

    @Test
    public void shouldListUniqueUsersMatchingTheSearchTermAcrossMultiplePasswordFiles() throws Exception {
        final GoPluginApiRequest request = mock(GoPluginApiRequest.class);

        mockPasswordFileWith("/var/etc/password_1.properties", "myname");
        mockPasswordFileWith("/var/etc/password_2.properties", "yournamed", "bar", "myname");
        mockPasswordFileWith("/var/etc/password_3.properties", "nameless", "foo");

        when(request.requestBody()).thenReturn(requestJsonWithMultipleAuthConfig("name"));

        final GoPluginApiResponse response = new SearchUserExecutor(request, fileReader).execute();

        assertThat(response.responseCode(), is(200));

        final String expectedResponseBody = "[\n" +
                "  {\n" +
                "    \"username\": \"myname\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"username\": \"yournamed\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"username\": \"nameless\"\n" +
                "  }\n" +
                "]";

        JSONAssert.assertEquals(expectedResponseBody, response.responseBody(), true);
    }

    private void mockPasswordFileWith(String passwordFilePath, String... users) throws IOException {
        final Properties properties = new Properties();
        for (String user : users) {
            properties.put(user, "password");
        }
        when(fileReader.read(passwordFilePath)).thenReturn(properties);
    }

    private String requestJson(String searchTerm) {
        return String.format("{\n" +
                "  \"search_term\": \"%s\",\n" +
                "  \"auth_configs\": [\n" +
                "    {\n" +
                "      \"id\": \"file\",\n" +
                "      \"configuration\": {\n" +
                "        \"PasswordFilePath\": \"/var/etc/password.properties\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}", searchTerm);
    }

    private String requestJsonWithMultipleAuthConfig(String searchTerm) {
        return String.format("{\n" +
                "  \"search_term\": \"%s\",\n" +
                "  \"auth_configs\": [\n" +
                "    {\n" +
                "      \"id\": \"file\",\n" +
                "      \"configuration\": {\n" +
                "        \"PasswordFilePath\": \"/var/etc/password_1.properties\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"file\",\n" +
                "      \"configuration\": {\n" +
                "        \"PasswordFilePath\": \"/var/etc/password_2.properties\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"file\",\n" +
                "      \"configuration\": {\n" +
                "        \"PasswordFilePath\": \"/var/etc/password_3.properties\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}", searchTerm);
    }
}