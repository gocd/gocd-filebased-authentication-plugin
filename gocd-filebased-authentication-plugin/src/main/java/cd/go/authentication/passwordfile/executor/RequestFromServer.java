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

public enum RequestFromServer {

    REQUEST_GET_PLUGIN_ICON(Constants.REQUEST_PREFIX + ".get-icon"),
    REQUEST_GET_CAPABILITIES(Constants.REQUEST_PREFIX + ".get-capabilities"),

    REQUEST_GET_AUTH_CONFIG_METADATA(String.join(".", Constants.REQUEST_PREFIX, Constants._AUTH_CONFIG_METADATA, "get-metadata")),
    REQUEST_AUTH_CONFIG_VIEW(String.join(".", Constants.REQUEST_PREFIX, Constants._AUTH_CONFIG_METADATA, "get-view")),
    REQUEST_VALIDATE_AUTH_CONFIG(String.join(".", Constants.REQUEST_PREFIX, Constants._AUTH_CONFIG_METADATA, "validate")),
    REQUEST_VERIFY_CONNECTION(String.join(".", Constants.REQUEST_PREFIX, Constants._AUTH_CONFIG_METADATA, "verify-connection")),

    REQUEST_AUTHENTICATE_USER(Constants.REQUEST_PREFIX + ".authenticate-user"),
    REQUEST_SEARCH_USERS(Constants.REQUEST_PREFIX + ".find-users"),

    REQUEST_DOES_USER_EXISTS(Constants.REQUEST_PREFIX + ".does-user-exists");

    private final String requestName;

    RequestFromServer(String requestName) {
        this.requestName = requestName;
    }

    public static RequestFromServer fromString(String requestName) {
        if (requestName != null) {
            for (RequestFromServer requestFromServer : RequestFromServer.values()) {
                if (requestName.equalsIgnoreCase(requestFromServer.requestName)) {
                    return requestFromServer;
                }
            }
        }

        throw new NoSuchRequestHandler("Request " + requestName + " is not supported by plugin.");
    }

    public String requestName() {
        return requestName;
    }

    private interface Constants {
        String REQUEST_PREFIX = "go.cd.authorization";
        String _AUTH_CONFIG_METADATA = "auth-config";
        String _ROLE_CONFIG_METADATA = "role-config";
    }
}

