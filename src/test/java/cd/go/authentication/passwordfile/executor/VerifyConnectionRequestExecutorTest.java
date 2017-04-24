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

import com.thoughtworks.go.plugin.api.request.DefaultGoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class VerifyConnectionRequestExecutorTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void execute_shouldValidateTheConfiguration() throws Exception {
        DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest(null, null, null);
        request.setRequestBody("{}");

        GoPluginApiResponse response = new VerifyConnectionRequestExecutor(request).execute();

        String expectedResponse = "{\"message\":\"Validation failed for the given Auth Config\",\"errors\":[{\"message\":\"PasswordFilePath must not be blank.\",\"key\":\"PasswordFilePath\"}],\"status\":\"validation-failed\"}";
        assertThat(response.responseBody(), is(expectedResponse));
    }

    @Test
    public void execute_shouldVerifyIfThePasswordFileExists() throws Exception {
        DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest(null, null, null);
        request.setRequestBody("{\"PasswordFilePath\": \"some_path\"}");

        GoPluginApiResponse response = new VerifyConnectionRequestExecutor(request).execute();

        String expectedResponse = "{\"message\":\"No password file at path `some_path`.\",\"status\":\"failure\"}";
        assertThat(response.responseBody(), is(expectedResponse));
    }

    @Test
    public void execute_shouldVerifyIfPasswordFilePathPointsToANormalFile() throws Exception {
        File folder = this.folder.newFolder("subfolder");

        DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest(null, null, null);
        request.setRequestBody(String.format("{\"PasswordFilePath\": \"%s\"}", folder.getAbsolutePath()));

        GoPluginApiResponse response = new VerifyConnectionRequestExecutor(request).execute();

        String expectedResponse = String.format("{\"message\":\"Password file path `%s` is not a normal file.\",\"status\":\"failure\"}", folder.getAbsolutePath());
        assertThat(response.responseBody(), is(expectedResponse));
    }

    @Test
    public void execute_shouldReturnASuccessResponseIfValidationAndVerificationPasses() throws Exception {
        File passwordFile = this.folder.newFile("password.properties");

        DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest(null, null, null);
        request.setRequestBody(String.format("{\"PasswordFilePath\": \"%s\"}", passwordFile.getAbsolutePath()));

        GoPluginApiResponse response = new VerifyConnectionRequestExecutor(request).execute();

        assertThat(response.responseBody(), is("{\"message\":\"Connection ok\",\"status\":\"success\"}"));
    }
}