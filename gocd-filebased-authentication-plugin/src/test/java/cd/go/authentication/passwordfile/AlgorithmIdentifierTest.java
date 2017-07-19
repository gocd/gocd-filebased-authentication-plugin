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
import cd.go.authentication.passwordfile.exception.NoSuchAlgorithmException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AlgorithmIdentifierTest {

    private AlgorithmIdentifier algorithmIdentifier;

    @Before
    public void setUp() throws Exception {
        algorithmIdentifier = new AlgorithmIdentifier();
    }

    @Test
    public void shouldIdentifySHA1Password() throws Exception {
        assertThat(algorithmIdentifier.identify("{SHA}W6ph5Mm5Pz8GgiULbPgzG37mj9g="), is(Algorithm.SHA1));
        assertThat(algorithmIdentifier.identify("W6ph5Mm5Pz8GgiULbPgzG37mj9g="), is(Algorithm.SHA1));
    }

    @Test
    public void shouldIdentifyBCryptAlgorithmFromPrefix() throws Exception {
        assertThat(algorithmIdentifier.identify("$2a$...$..."), is(Algorithm.BCRYPT));
        assertThat(algorithmIdentifier.identify("$2A$...$..."), is(Algorithm.BCRYPT));
        assertThat(algorithmIdentifier.identify("$2b$...$..."), is(Algorithm.BCRYPT));
        assertThat(algorithmIdentifier.identify("$2B$...$..."), is(Algorithm.BCRYPT));
        assertThat(algorithmIdentifier.identify("$2Y$...$..."), is(Algorithm.BCRYPT));
        assertThat(algorithmIdentifier.identify("$2y$...$..."), is(Algorithm.BCRYPT));
    }

    @Test
    public void shouldIdentifyPBKDF2WithHmacSHA1FromPrefix() throws Exception {
        assertThat(algorithmIdentifier.identify("$PBKDF2WithHmacSHA1$...$..."), is(Algorithm.PBKDF2WithHmacSHA1));
        assertThat(algorithmIdentifier.identify("$pbkdf2withhmacsha1$...$..."), is(Algorithm.PBKDF2WithHmacSHA1));
    }

    @Test
    public void shouldIdentifyPBKDF2WithHmacSHA256FromPrefix() throws Exception {
        assertThat(algorithmIdentifier.identify("$PBKDF2WithHmacSHA256$...$..."), is(Algorithm.PBKDF2WithHmacSHA256));
        assertThat(algorithmIdentifier.identify("$pbkdf2withhmacsha256$...$..."), is(Algorithm.PBKDF2WithHmacSHA256));
    }

    @Test(expected = NoSuchAlgorithmException.class)
    public void shouldErrorOutIfItFailedToDetectAlgorithm() throws Exception {
        algorithmIdentifier.identify("$foo");
    }

}