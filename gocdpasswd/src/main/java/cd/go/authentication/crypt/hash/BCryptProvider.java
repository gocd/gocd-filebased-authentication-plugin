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

package cd.go.authentication.crypt.hash;

import cd.go.authentication.crypt.CliArguments;
import org.mindrot.jbcrypt.BCrypt;

import static java.text.MessageFormat.format;

public class BCryptProvider implements HashProvider {

    @Override
    public String hash(CliArguments arguments) {
        final String salt = BCrypt.gensalt(arguments.cost());

        final String hashedPasswd = BCrypt.hashpw(arguments.password(), salt);

        return format("{0}={1}", arguments.username(), hashedPasswd);
    }
}
