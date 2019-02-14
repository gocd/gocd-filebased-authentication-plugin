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
import cd.go.authentication.passwordfile.model.AuthConfig;
import cd.go.authentication.passwordfile.model.User;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Properties;

import static cd.go.authentication.passwordfile.utils.Util.GSON;

public class IsValidUserRequestExecutor implements RequestExecutor {
    private final GoPluginApiRequest request;
    private PasswordFileReader passwordFileReader;

    public IsValidUserRequestExecutor(GoPluginApiRequest request) {
        this(request, new PasswordFileReader());
    }

    protected IsValidUserRequestExecutor(GoPluginApiRequest request, PasswordFileReader passwordFileReader) {
        this.request = request;
        this.passwordFileReader = passwordFileReader;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        JsonObject jsonObject = GSON.fromJson(request.requestBody(), JsonObject.class);
        Type type = new TypeToken<AuthConfig>() {}.getType();

        String username = jsonObject.get("username").getAsString();
        AuthConfig authConfig = GSON.fromJson(jsonObject.get("auth_config").toString(), type);

        final User found = find(username, authConfig);

        if (found != null) {
            return new DefaultGoPluginApiResponse(200);
        }

        return new DefaultGoPluginApiResponse(404);
    }

    public User find(String username, AuthConfig authConfig) throws IOException {
        final Properties properties = passwordFileReader.read(authConfig.getConfiguration().getPasswordFilePath());
        return findUserNameContaining(username, properties);
    }

    private User findUserNameContaining(String usernameToFind, Properties properties) {
        for (Object o : properties.keySet()) {
            String username = (String) o;
            if (username.toLowerCase().equalsIgnoreCase(usernameToFind.toLowerCase())) {
                return new User(username, null, null);
            }
        }

        return null;
    }

}
