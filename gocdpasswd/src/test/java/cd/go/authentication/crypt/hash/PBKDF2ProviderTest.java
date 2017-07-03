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

package cd.go.authentication.crypt.hash;

import cd.go.authentication.crypt.Algorithm;
import cd.go.authentication.crypt.CliArguments;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PBKDF2ProviderTest {

    private PBKDF2Provider provider;
    private CliArguments cliArguments;

    @Before
    public void setUp() throws Exception {
        provider = new PBKDF2Provider();
        cliArguments = mock(CliArguments.class);

        when(cliArguments.username()).thenReturn("admin");
        when(cliArguments.password()).thenReturn("badger");
    }

    @Test
    public void shouldGeneratePasswordFileEntryUsingPBKDF2WithHmacSHA1() throws Exception {
        when(cliArguments.algorithm()).thenReturn(Algorithm.PBKDF2WithHmacSHA1);
        when(cliArguments.iterations()).thenReturn(1000);
        when(cliArguments.keyLength()).thenReturn(256);
        when(cliArguments.salt()).thenReturn("812A9B665D09904B8239778EC8D18CF7");

        final String hash = provider.hash(cliArguments);

        assertThat(hash, is("admin=$PBKDF2WithHmacSHA1$1000$256$812A9B665D09904B8239778EC8D18CF7$093763FF7F9A2E279940FFB89B341A4DDE5780E7046F248BA36FF9A9F5A5E79E"));
    }

    @Test
    public void shouldGeneratePasswordFileEntryUsingPBKDF2WithHmacSHA256() throws Exception {
        when(cliArguments.algorithm()).thenReturn(Algorithm.PBKDF2WithHmacSHA256);
        when(cliArguments.iterations()).thenReturn(2000);
        when(cliArguments.keyLength()).thenReturn(512);
        when(cliArguments.salt()).thenReturn("812A9B665D09904B8239778EC8D18CF7");

        final String hash = provider.hash(cliArguments);

        assertThat(hash, is("admin=$PBKDF2WithHmacSHA256$2000$512$812A9B665D09904B8239778EC8D18CF7$CE4952378009E7706F3E5B8DF33DEE6CCC53448460CB093353DF04800A22BD412F5F2F76BDBD1EACE832534B1BEC5F96FC115261967F67864D187D5860DB0470"));
    }
}
