/*
 * Copyright 2019 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cd.go.authentication.passwordfile.executor;

import cd.go.authentication.passwordfile.PasswordFileReader;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IsValidUserRequestExecutorTest {

    private PasswordFileReader fileReader;

    @BeforeEach
    public void setUp() throws Exception {
        fileReader = mock(PasswordFileReader.class);
    }

    @Test
    public void shouldReturn200WhenTheCurrentUserExistsInThePasswordFile() throws Exception {
        final GoPluginApiRequest request = mock(GoPluginApiRequest.class);

        final Properties properties = new Properties();
        properties.put("bob", "password1");
        when(fileReader.read("/var/etc/password.properties")).thenReturn(properties);
        when(request.requestBody()).thenReturn(requestJson("bob"));

        final GoPluginApiResponse response = new IsValidUserRequestExecutor(request, fileReader).execute();

        assertThat(response.responseCode(), is(200));
    }

    @Test
    public void shouldReturn404WhenTheCurrentUserDoesNotExistsInThePasswordFile() throws Exception {
        final GoPluginApiRequest request = mock(GoPluginApiRequest.class);

        final Properties properties = new Properties();
        properties.put("bob", "password1");
        when(fileReader.read("/var/etc/password.properties")).thenReturn(properties);
        when(request.requestBody()).thenReturn(requestJson("john"));

        final GoPluginApiResponse response = new IsValidUserRequestExecutor(request, fileReader).execute();

        assertThat(response.responseCode(), is(404));
    }

    @Test
    public void shouldLookForExactUsernameMatchInsteadOfRegExp() throws Exception {
        final GoPluginApiRequest request = mock(GoPluginApiRequest.class);

        final Properties properties = new Properties();
        properties.put("bobby", "password1");
        when(fileReader.read("/var/etc/password.properties")).thenReturn(properties);
        when(request.requestBody()).thenReturn(requestJson("bob"));

        final GoPluginApiResponse response = new IsValidUserRequestExecutor(request, fileReader).execute();

        assertThat(response.responseCode(), is(404));
    }

    @Test
    public void shouldLookForCaseInsensitiveUsernameMatch() throws Exception {
        final GoPluginApiRequest request = mock(GoPluginApiRequest.class);

        final Properties properties = new Properties();
        properties.put("bob", "password1");
        when(fileReader.read("/var/etc/password.properties")).thenReturn(properties);
        when(request.requestBody()).thenReturn(requestJson("BoB"));

        final GoPluginApiResponse response = new IsValidUserRequestExecutor(request, fileReader).execute();

        assertThat(response.responseCode(), is(200));
    }

    private String requestJson(String username) {
        return String.format("{\n" +
                "  \"username\": \"%s\",\n" +
                "  \"auth_config\": {\n" +
                "      \"id\": \"file\",\n" +
                "      \"configuration\": {\n" +
                "        \"PasswordFilePath\": \"/var/etc/password.properties\"\n" +
                "      }\n" +
                "    }\n" +
                "}", username);
    }
}
