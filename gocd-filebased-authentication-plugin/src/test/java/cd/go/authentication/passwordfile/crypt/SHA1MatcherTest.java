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

package cd.go.authentication.passwordfile.crypt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SHA1MatcherTest {

    @Test
    public void shouldReturnTrueForValidPassword() throws Exception {
        HashMatcher sha1 = new SHA1Matcher();

        final boolean matching = sha1.matches("badger", "ThmbShxAtJepX80c2JY1FzOEmUk=");

        assertTrue(matching);
    }

    @Test
    public void shouldReturnFalseForInvalidPassword() throws Exception {
        HashMatcher sha1 = new SHA1Matcher();

        final boolean matching = sha1.matches("random", "ThmbShxAtJepX80c2JY1FzOEmUk=");

        assertFalse(matching);
    }

    @Test
    public void shouldValidateHashWithSHAPrefix() throws Exception {
        HashMatcher sha1 = new SHA1Matcher();

        final boolean matching = sha1.matches("badger", "{SHA}ThmbShxAtJepX80c2JY1FzOEmUk=");

        assertTrue(matching);
    }
}