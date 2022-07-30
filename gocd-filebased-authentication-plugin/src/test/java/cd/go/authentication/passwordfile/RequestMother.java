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

package cd.go.authentication.passwordfile;

import com.thoughtworks.go.plugin.api.request.DefaultGoApiRequest;
import com.thoughtworks.go.plugin.api.request.GoApiRequest;

public class RequestMother {
    public static GoApiRequest requestWithCredentials(String username, String password) {
        final DefaultGoApiRequest defaultGoApiRequest = new DefaultGoApiRequest(null, null, null);
        defaultGoApiRequest.setRequestBody(requestJson(username, password));
        return defaultGoApiRequest;
    }

    private static String requestJson(String username, String password) {
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
