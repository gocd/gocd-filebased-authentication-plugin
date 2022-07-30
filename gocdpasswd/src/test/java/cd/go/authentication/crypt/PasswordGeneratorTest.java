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

import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class PasswordGeneratorTest {

    @Test
    public void shouldGeneratePasswordFileEntryUsingBCrypt() throws Exception {
        final String[] args = {"-B", "-u", "admin", "-p", "badger"};
        PasswordGenerator generator = spy(new PasswordGenerator());

        generator.generate(args);

        verify(generator).printResult(startsWith("admin=$2a$05$"));
    }

    @Test
    public void shouldGeneratePasswordFileEntryUsingPBKDF2WithHmacSHA1() throws Exception {
        final String[] args = {"-P", "-u", "admin", "-p", "badger"};
        PasswordGenerator generator = spy(new PasswordGenerator());

        generator.generate(args);

        verify(generator).printResult(startsWith("admin=$PBKDF2WithHmacSHA1$1000$256$"));
    }

    @Test
    public void shouldGeneratePasswordFileEntryUsingPBKDF2WithHmacSHA256() throws Exception {
        final String[] args = {"-S", "-u", "admin", "-p", "badger"};
        PasswordGenerator generator = spy(new PasswordGenerator());

        generator.generate(args);

        verify(generator).printResult(startsWith("admin=$PBKDF2WithHmacSHA256$1000$256$"));
    }
}