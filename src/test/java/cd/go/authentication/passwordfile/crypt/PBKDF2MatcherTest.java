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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PBKDF2MatcherTest {
    private HashMatcher pbkdf2Matcher;

    @Before
    public void setUp() throws Exception {
        pbkdf2Matcher = new PBKDF2Matcher();
    }

    @Test
    public void shouldValidatePBKDF2WithHmacSHA256Hash() throws Exception {
        final String hashed = new StringBuilder()
                .append("$PBKDF2WithHmacSHA256")
                .append("$1000")
                .append("$256")
                .append("$8C6A56243A51EEDA40A9DD0ECCD1B55E")
                .append("$48FE9A232A70EE8E108BF07DCE80C966CF57BE35F8685469E2798F6820552284").toString();


        final boolean matches = pbkdf2Matcher.matches("password", hashed);

        assertTrue(matches);
    }

    @Test
    public void shouldValidatePBKDF2WithHmacSHA1Hash() throws Exception {
        final String hashed = new StringBuilder()
                .append("$PBKDF2WithHmacSHA1")
                .append("$1000")
                .append("$256")
                .append("$85A1CCB7C4F4C535CBABDDBD01FECB7B")
                .append("$FA487EEB17946558C49055E4ECB3FAA02A3C847738DF67231E5D1A5F33FBA7FC").toString();

        final boolean matches = pbkdf2Matcher.matches("badger", hashed);

        assertTrue(matches);
    }

    @Test
    public void shouldReturnFalseIfComputedHashIsDifferent() throws Exception {
        final String hashed = new StringBuilder()
                .append("$PBKDF2WithHmacSHA1")
                .append("$1000")
                .append("$256")
                .append("$85A1CCB7C4F4C535CBABDDBD01FECB7B")
                .append("$FF8DE0A276CDBC3E01A87E804B910753D214FAB1D9DB8334F801A29EC5D7A179").toString();

        final boolean matches = pbkdf2Matcher.matches("badger", hashed);

        assertFalse(matches);
    }
}