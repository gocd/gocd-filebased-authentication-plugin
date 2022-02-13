/*
 * Copyright 2017 ThoughtWorks, Inc.
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

import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.hamcrest.MatcherAssert.assertThat;

public class GetCapabilitiesExecutorTest {

    @Test
    public void shouldExposeItsCapabilities() throws Exception {
        GoPluginApiResponse response = new GetCapabilitiesExecutor().execute();

        assertThat(response.responseCode(), CoreMatchers.is(200));

        String expectedJSON = "{\n" +
                "    \"supported_auth_type\":\"password\",\n" +
                "    \"can_search\":true,\n" +
                "    \"can_authorize\":false,\n" +
                "    \"can_get_user_roles\":false\n" +
                "}";

        JSONAssert.assertEquals(expectedJSON, response.responseBody(), true);
    }
}
