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

import com.beust.jcommander.ParameterException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CliArgumentsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldParsePBKDF2CliArgumentsToObject() throws Exception {
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
    public void shouldParseBCryptCliArgumentsToObject() throws Exception {
        String[] args = {"-B", "-u", "admin", "-p", "badger", "-C", "10"};

        final CliArguments arguments = CliArguments.fromArgs(args);

        assertThat(arguments.algorithm(), is(Algorithm.BCRYPT));
        assertThat(arguments.username(), is("admin"));
        assertThat(arguments.password(), is("badger"));
        assertThat(arguments.cost(), is(10));
    }

    @Test
    public void shouldErrorOutEncryptionAlgorithmIsNotSpecified() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Expected encryption algorithm: [-B, -P, -S]");

        String[] args = {"-u", "admin", "-p", "badger"};

        CliArguments.fromArgs(args);
    }

    @Test
    public void shouldErrorOutOnInvalidBCryptArguments() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Argument to -C must be an integer value: 4 to 31");

        String[] args = {"-B", "-u", "admin", "-p", "badger", "-C", "1"};

        CliArguments.fromArgs(args);
    }

    @Test
    public void shouldErrorOutIfRequiredOptionsMissingInCliArguments() throws Exception {
        thrown.expect(ParameterException.class);
        thrown.expectMessage("The following option is required: [-u]");

        String[] args = {"-p", "badger"};

        CliArguments.fromArgs(args);
    }

    @Test
    public void shouldPromptForPasswordIfNotProvidedAsArgument() throws Exception {
        final PasswordReader passwordReader = mock(PasswordReader.class);
        when(passwordReader.readPassword()).thenReturn("password-from-cli");

        final CliArguments arguments = new CliArguments(passwordReader);

        final String password = arguments.password();

        assertThat(password, is("password-from-cli"));
    }
}