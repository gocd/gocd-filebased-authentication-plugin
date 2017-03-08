package cd.go.authentication.passwordfile.executor;

import cd.go.authentication.passwordfile.Authenticator;
import cd.go.authentication.passwordfile.exception.AuthenticationException;
import cd.go.authentication.passwordfile.model.AuthConfig;
import cd.go.authentication.passwordfile.model.Credentials;
import cd.go.authentication.passwordfile.model.User;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserAuthenticationExecutorTest {

    private Authenticator authenticator;

    @Before
    public void setUp() throws Exception {
        authenticator = mock(Authenticator.class);
    }

    @Test
    public void shouldAuthenticate() throws Exception {
        final GoPluginApiRequest goApiRequest = mock(GoPluginApiRequest.class);
        final String requestBody = requestJson("username", "password");

        when(goApiRequest.requestBody()).thenReturn(requestBody);
        final User user = new User("username", "displayName", "email");
        when(authenticator.authenticate(Credentials.fromJSON(requestBody), AuthConfig.fromJSONList(requestBody))).thenReturn(user);

        final GoPluginApiResponse response = new UserAuthenticationExecutor(goApiRequest, authenticator).execute();

        assertThat(response.responseCode(), is(200));

        final String expectedResponseBody = "{\n" +
                "  \"roles\": [],\n" +
                "  \"user\": {\n" +
                "    \"username\": \"username\",\n" +
                "    \"display_name\": \"displayName\",\n" +
                "    \"email\": \"email\"\n" +
                "  }\n" +
                "}";

        JSONAssert.assertEquals(expectedResponseBody, response.responseBody(), true);
    }

    @Test
    public void shouldReturnAEmptyResponseOnAuthenticationFailure() throws Exception {
        final GoPluginApiRequest goApiRequest = mock(GoPluginApiRequest.class);
        final String requestBody = requestJson("username", "password");

        when(goApiRequest.requestBody()).thenReturn(requestBody);
        when(authenticator.authenticate(Credentials.fromJSON(requestBody), AuthConfig.fromJSONList(requestBody))).thenThrow(new AuthenticationException("failed"));

        final GoPluginApiResponse response = new UserAuthenticationExecutor(goApiRequest, authenticator).execute();

        assertThat(response.responseCode(), is(200));

        JSONAssert.assertEquals("{}", response.responseBody(), true);
    }

    private String requestJson(String username, String password) {
        return String.format("{\n" +
                "  \"credentials\": {\n" +
                "    \"username\": \"%s\",\n" +
                "    \"password\": \"%s\"\n" +
                "  },\n" +
                "  \"auth_configs\": [\n" +
                "    {\n" +
                "      \"id\": \"file\",\n" +
                "      \"configuration\": {\n" +
                "        \"PasswordFilePath\": \"password/file/path\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}", username, password);
    }
}