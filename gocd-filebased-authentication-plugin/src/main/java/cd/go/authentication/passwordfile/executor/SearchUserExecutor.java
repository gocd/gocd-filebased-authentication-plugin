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
import cd.go.authentication.passwordfile.model.AuthConfig;
import cd.go.authentication.passwordfile.model.User;
import cd.go.authentication.passwordfile.utils.Util;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.io.IOException;
import java.util.*;

import static cd.go.authentication.passwordfile.PasswordFilePlugin.LOG;

public class SearchUserExecutor implements RequestExecutor {
    public static final String SEARCH_TERM = "search_term";
    private final GoPluginApiRequest request;
    private final PasswordFileReader passwordFileReader;

    public SearchUserExecutor(GoPluginApiRequest request) {
        this(request, new PasswordFileReader());
    }

    protected SearchUserExecutor(GoPluginApiRequest request, PasswordFileReader passwordFileReader) {
        this.request = request;
        this.passwordFileReader = passwordFileReader;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        Map<String, String> requestParam = Util.GSON.fromJson(request.requestBody(), Map.class);
        String searchTerm = requestParam.get(SEARCH_TERM);
        List<AuthConfig> authConfigs = AuthConfig.fromJSONList(request.requestBody());

        final Set<User> users = searchUsers(searchTerm, authConfigs);

        return new DefaultGoPluginApiResponse(200, Util.GSON.toJson(users));
    }

    Set<User> searchUsers(String searchTerm, List<AuthConfig> authConfigs) throws IOException {
        final HashSet<User> users = new HashSet<>();
        for (AuthConfig authConfig : authConfigs) {
            try {
                LOG.info(String.format("[User Search] Looking up for users matching search_term: `%s`" +
                        " using auth_config: `%s`", searchTerm, authConfig.getId()));
                users.addAll(search(searchTerm, authConfig));
            } catch (Exception e) {
                LOG.error(String.format("[User Search] Error while searching users with auth_config: '%s'", authConfig.getId()), e);
            }
        }
        return users;
    }

    public Set<User> search(String searchText, AuthConfig authConfig) throws IOException {
        final Properties properties = passwordFileReader.read(authConfig.getConfiguration().getPasswordFilePath());
        Set<User> users = findUserNameContaining(searchText, properties);
        return users;
    }

    private Set<User> findUserNameContaining(String searchText, Properties properties) {
        Set<User> users = new HashSet<>();
        for (Object o : properties.keySet()) {
            String username = (String) o;
            if (username.toLowerCase().contains(searchText.toLowerCase())) {
                users.add(new User(username, null, null));
            }
        }
        return users;
    }

}
