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

import cd.go.authentication.crypt.Algorithm;
import cd.go.authentication.crypt.CliArguments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;

public class BCryptProviderTest {
    private CliArguments cliArguments;
    private BCryptProvider provider;

    @BeforeEach
    public void setUp() throws Exception {
        provider = new BCryptProvider();

        cliArguments = mock(CliArguments.class);

        when(cliArguments.username()).thenReturn("admin");
        when(cliArguments.password()).thenReturn("badger");
    }

    @Test
    public void shouldGeneratePasswordFileEntryUsingBCrypt() throws Exception {
        when(cliArguments.algorithm()).thenReturn(Algorithm.BCRYPT);
        when(cliArguments.cost()).thenReturn(6);

        final String hash = provider.hash(cliArguments);

        assertThat(hash, startsWith("admin=$2a$06$"));
    }
}