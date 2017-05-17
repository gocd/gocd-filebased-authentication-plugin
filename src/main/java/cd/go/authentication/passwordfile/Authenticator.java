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

package cd.go.authentication.passwordfile;

import cd.go.authentication.passwordfile.exception.AuthenticationException;
import cd.go.authentication.passwordfile.model.AuthConfig;
import cd.go.authentication.passwordfile.model.Credentials;
import cd.go.authentication.passwordfile.model.User;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import static cd.go.authentication.passwordfile.PasswordFilePlugin.LOG;

public class Authenticator {
    final PasswordFileReader passwordFileReader;

    public Authenticator() {
        this(new PasswordFileReader());
    }

    public Authenticator(PasswordFileReader passwordFileReader) {
        this.passwordFileReader = passwordFileReader;
    }

    public User authenticate(Credentials credentials, List<AuthConfig> authConfigs) throws IOException, NoSuchAlgorithmException {

        for (AuthConfig authConfig : authConfigs) {
            LOG.info(String.format("[Authenticate] Authenticating User: %s using auth_config: %s", credentials.getUsername(), authConfig.getId()));
            try {
                final String passwordFilePath = authConfig.getConfiguration().getPasswordFilePath();
                final Properties usernamePassword = passwordFileReader.read(passwordFilePath);

                if (!usernamePassword.containsKey(credentials.getUsername())) {
                    continue;
                }

                final String password = stripShaFromPasswordIfRequired(usernamePassword.getProperty(credentials.getUsername()));

                if (password.equals(sha1Digest(credentials.getPassword().getBytes()))) {
                    LOG.info(String.format("[Authenticate] User `%s` successfully authenticated using auth_config: %s", credentials.getUsername(), authConfig.getId()));
                    return new User(credentials.getUsername(), null, null);
                }
            } catch (Exception e) {
                LOG.error(String.format("[Authenticate] Error authenticating User: %s using auth_config: %s", credentials.getUsername(), authConfig.getId()), e);
            }
        }

        throw new AuthenticationException("Unable to authenticate user, user not found or bad credentials");
    }

    private String stripShaFromPasswordIfRequired(String password) {
        if (password.startsWith("{SHA}")) {
            return password.substring(5);
        }
        return password;
    }

    private String sha1Digest(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        return Base64.getEncoder().encodeToString(md.digest(bytes));
    }
}