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

package cd.go.authentication.passwordfile.crypt;

import cd.go.authentication.passwordfile.AlgorithmIdentifier;
import cd.go.authentication.passwordfile.exception.InvalidFormatException;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PBKDF2HashInfoTest {

    @Test
    public void shouldPBKDF2HashFormatToObject() throws Exception {
        final String hashed = new StringBuilder()
                .append("$PBKDF2WithHmacSHA256")
                .append("$2000")
                .append("$256")
                .append("$85A1CCB7C4F4C535CBABDDBD01FECB7B")
                .append("$84b44c923afeaad550305e30aceee15f68cc3821f8394e3a3609d5b06649f64b").toString();

        final AlgorithmIdentifier algorithmIdentifier = mock(AlgorithmIdentifier.class);
        when(algorithmIdentifier.identify(hashed)).thenReturn(Algorithm.PBKDF2WithHmacSHA256);

        PBKDF2HashInfo info = new PBKDF2HashInfo(hashed, algorithmIdentifier);

        assertThat(info.getAlgorithm(), is(Algorithm.PBKDF2WithHmacSHA256));
        assertThat(info.getIterations(), is(2000));
        assertThat(info.getKeyLength(), is(256));
        assertThat(info.getSalt(), is("85A1CCB7C4F4C535CBABDDBD01FECB7B"));
        assertThat(info.getHashedPassword(), is("84b44c923afeaad550305e30aceee15f68cc3821f8394e3a3609d5b06649f64b"));
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldErrorOutIfBadHashFormat() throws Exception {
        final String hashed = new StringBuilder()
                .append("$PBKDF2WithHmacSHA256")
                .append("$2000")
                .append("$256").toString();

        new PBKDF2HashInfo(hashed);
    }
}