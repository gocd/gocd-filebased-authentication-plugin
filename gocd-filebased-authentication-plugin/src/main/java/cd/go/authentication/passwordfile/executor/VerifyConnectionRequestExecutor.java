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


import cd.go.authentication.passwordfile.model.Configuration;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cd.go.authentication.passwordfile.utils.Util.isBlank;

public class VerifyConnectionRequestExecutor implements RequestExecutor {
    private static final Gson GSON = new Gson();
    private final GoPluginApiRequest request;

    public VerifyConnectionRequestExecutor(GoPluginApiRequest request) {
        this.request = request;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        List<Map<String, String>> errors = validateAuthConfig();
        if (errors.size() != 0) {
            return validationFailureResponse(errors);
        }

        VerifyConnectionResult result = verifyConnection();
        if (!result.isSuccessful()) {
            return verifyConnectionFailureResponse(result.getError());
        }

        return successResponse();
    }

    private VerifyConnectionResult verifyConnection() {
        VerifyConnectionResult result = new VerifyConnectionResult();
        Configuration configuration = Configuration.fromJSON(request.requestBody());
        File file = new File(configuration.getPasswordFilePath());

        try {
            if(!file.exists()) {
                result.setError(String.format("No password file at path `%s`.", configuration.getPasswordFilePath()));
                return result;
            }

            if (!file.isFile()) {
                result.setError(String.format("Password file path `%s` is not a normal file.", configuration.getPasswordFilePath()));
                return result;
            }

            if (!file.canRead()) {
                result.setError(String.format("Unable to read password file `%s`, check permissions."));
            }
        } catch (SecurityException e) {
            result.setError(e.getMessage());
        }

        return result;
    }

    private List<Map<String, String>> validateAuthConfig() {
        Map<String, String> configuration = GSON.fromJson(request.requestBody(), Map.class);

        return Configuration.validate(configuration);
    }

    private GoPluginApiResponse successResponse() {
        return respondWith("success", "Connection ok", null);
    }

    private GoPluginApiResponse verifyConnectionFailureResponse(String errorMessage) {
        return respondWith("failure", errorMessage, null);
    }

    private GoPluginApiResponse validationFailureResponse(List<Map<String, String>> errors) {
        return respondWith("validation-failed", "Validation failed for the given Auth Config", errors);
    }

    private GoPluginApiResponse respondWith(String status, String message, List<Map<String, String>> errors) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);

        if (errors != null && errors.size() > 0) {
            response.put("errors", errors);
        }

        return DefaultGoPluginApiResponse.success(GSON.toJson(response));
    }

    class VerifyConnectionResult {
        private String error;

        public boolean isSuccessful() {
            return isBlank(error);
        }

        public void setError(String errorMessage) {
            this.error = errorMessage;
        }

        public String getError() {
            return this.error;
        }
    }
}
