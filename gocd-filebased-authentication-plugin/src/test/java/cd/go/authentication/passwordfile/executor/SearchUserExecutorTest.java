/*
 * Copyright 2022 Thoughtworks, Inc.
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
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchUserExecutorTest {

    private PasswordFileReader fileReader;

    @BeforeEach
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