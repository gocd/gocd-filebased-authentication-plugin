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

import cd.go.authentication.passwordfile.crypt.Algorithm;
import cd.go.authentication.passwordfile.exception.AuthenticationException;
import cd.go.authentication.passwordfile.model.AuthConfig;
import cd.go.authentication.passwordfile.model.Credentials;
import cd.go.authentication.passwordfile.model.User;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static cd.go.authentication.passwordfile.PasswordFilePlugin.LOG;

public class Authenticator {
    final PasswordFileReader passwordFileReader;
    private final AlgorithmIdentifier algorithmIdentifier;

    public Authenticator() {
        this(new PasswordFileReader(), new AlgorithmIdentifier());
    }

    public Authenticator(PasswordFileReader passwordFileReader, AlgorithmIdentifier algorithmIdentifier) {
        this.passwordFileReader = passwordFileReader;
        this.algorithmIdentifier = algorithmIdentifier;
    }

    public User authenticate(Credentials credentials, List<AuthConfig> authConfigs) throws IOException, NoSuchAlgorithmException {

        for (AuthConfig authConfig : authConfigs) {
            LOG.info(String.format("[Authenticate] Authenticating User: %s using auth_config: %s", credentials.getUsername(), authConfig.getId()));
            try {
                final String passwordFilePath = authConfig.getConfiguration().getPasswordFilePath();
                LOG.info(String.format("[Authenticate] Fetching all user information from password file: %s", passwordFilePath));
                Map<String, UserDetails> userMap = buildUserMap(passwordFileReader.read(passwordFilePath));
                LOG.info("[Authenticate] Done fetching all user information from password file.");
                UserDetails userDetails = userMap.get(credentials.getUsername().toLowerCase());

                if (userDetails == null) {
                    LOG.info(String.format("[Authenticate] No user found with username: %s in auth config %s.", credentials.getUsername(), authConfig.getId()));
                    continue;
                }

                LOG.info(String.format("[Authenticate] Finding algorithm used for hashing password of user %s.", userDetails.username));
                Algorithm algorithm = algorithmIdentifier.identify(userDetails.password);
                LOG.info(String.format("[Authenticate] Algorithm found: %s.", algorithm.getName()));

                if (algorithm == Algorithm.SHA1) {
                    LOG.warn(String.format("[Authenticate] User `%s`'s password is hashed using SHA1. Please use bcrypt hashing instead.", credentials.getUsername()));
                }

                LOG.info("[Authenticate] Start matching input password with hashed password.");
                if (algorithm.matcher().matches(credentials.getPassword(), userDetails.password)) {
                    LOG.info(String.format("[Authenticate] User `%s` successfully authenticated using auth config: %s", credentials.getUsername(), authConfig.getId()));
                    return new User(userDetails.username, userDetails.username, null);
                }
            } catch (Exception e) {
                LOG.info(String.format("[Authenticate] Failed to authenticate user: %s using auth_config: %s", credentials.getUsername(), authConfig.getId()));
                LOG.debug("Exception: ", e);
            }
        }

        throw new AuthenticationException("Unable to authenticate user, user not found or bad credentials");
    }

    private Map<String, UserDetails> buildUserMap(Properties props) {
        HashMap<String, UserDetails> userMap = new HashMap<>();

        Iterator<Object> iterator = props.keySet().iterator();
        while (iterator.hasNext()) {
            String username = (String) iterator.next();
            String password = props.getProperty(username);

            userMap.put(username.toLowerCase(), new UserDetails(username, password));
        }
        return userMap;
    }

    private class UserDetails {
        private final String username;
        private final String password;

        public UserDetails(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}