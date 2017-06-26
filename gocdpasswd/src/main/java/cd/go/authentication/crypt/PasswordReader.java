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

package cd.go.authentication.crypt;

import java.io.Console;

public class PasswordReader {
    public String readPassword() {
        final Console console = System.console();
        try {
            String newPassword = new String(console.readPassword("New password: "));
            String reTypePassword = new String(console.readPassword("Re-type new password: "));

            if (newPassword.equals(reTypePassword)) {
                return newPassword;
            }
        } finally {
            console.flush();
        }

        throw new RuntimeException("Password verification error.");
    }
}
