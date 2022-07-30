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

package cd.go.authentication.crypt;

import com.beust.jcommander.ParameterException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CliArgumentsTest {

    @Test
    public void shouldParsePBKDF2CliArgumentsToObject() {
        String[] args = {"-P", "-u", "admin", "-p", "badger", "-i", "2000", "-l", "64", "-s", "812A9B665D09904B8239778EC8D18CF7"};

        final CliArguments arguments = CliArguments.fromArgs(args);

        assertThat(arguments.algorithm(), is(Algorithm.PBKDF2WithHmacSHA1));
        assertThat(arguments.username(), is("admin"));
        assertThat(arguments.password(), is("badger"));
        assertThat(arguments.iterations(), is(2000));
        assertThat(arguments.keyLength(), is(512));
        assertThat(arguments.salt(), is("812A9B665D09904B8239778EC8D18CF7"));
    }

    @Test
    public void shouldParseBCryptCliArgumentsToObject() {
        String[] args = {"-B", "-u", "admin", "-p", "badger", "-C", "10"};

        final CliArguments arguments = CliArguments.fromArgs(args);

        assertThat(arguments.algorithm(), is(Algorithm.BCRYPT));
        assertThat(arguments.username(), is("admin"));
        assertThat(arguments.password(), is("badger"));
        assertThat(arguments.cost(), is(10));
    }

    @Test
    public void shouldErrorOutEncryptionAlgorithmIsNotSpecified() {
        String[] args = {"-u", "admin", "-p", "badger"};

        Exception exception = assertThrows(IllegalArgumentException.class, () -> CliArguments.fromArgs(args));
        assertThat(exception.getMessage(), is("Expected encryption algorithm: [-B, -P, -S]"));
    }

    @Test
    public void shouldErrorOutOnInvalidBCryptArguments() {
        String[] args = {"-B", "-u", "admin", "-p", "badger", "-C", "1"};

        Exception exception = assertThrows(IllegalArgumentException.class, () -> CliArguments.fromArgs(args));
        assertThat(exception.getMessage(), is("Argument to -C must be an integer value: 4 to 31"));
    }

    @Test
    public void shouldErrorOutIfRequiredOptionsMissingInCliArguments() {
        String[] args = {"-p", "badger"};

        Exception exception = assertThrows(ParameterException.class, () -> CliArguments.fromArgs(args));
        assertThat(exception.getMessage(), is("The following option is required: [-u]"));
    }

    @Test
    public void shouldPromptForPasswordIfNotProvidedAsArgument() {
        final PasswordReader passwordReader = mock(PasswordReader.class);
        when(passwordReader.readPassword()).thenReturn("password-from-cli");

        final CliArguments arguments = new CliArguments(passwordReader);

        final String password = arguments.password();

        assertThat(password, is("password-from-cli"));
    }
}