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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BCryptMatcherTest {

    private HashMatcher bcryptMatcher;

    @BeforeEach
    public void setUp() throws Exception {
        bcryptMatcher = new BCryptMatcher();
    }

    @Test
    public void shouldReturnTrueForValidPassword() throws Exception {
        final boolean matches = bcryptMatcher.matches("bob", "$2y$05$6x2HXaQcqi7osijESYtUTeJBx5rYpIYQJlP51rFZCRpuE3hBH/LAq");

        assertTrue(matches);
    }

    @Test
    public void shouldReturnFalseForInvalidPassword() throws Exception {
        final boolean matches = bcryptMatcher.matches("bob", "$2y$05$6x2HXaQcqi7osijESYtUTeJBx5rYpIYQJlP51rFZCRpuE3hBH/dad");

        assertFalse(matches);
    }
}