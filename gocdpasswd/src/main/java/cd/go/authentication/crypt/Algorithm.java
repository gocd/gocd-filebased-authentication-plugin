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

import cd.go.authentication.crypt.hash.BCryptProvider;
import cd.go.authentication.crypt.hash.HashProvider;
import cd.go.authentication.crypt.hash.PBKDF2Provider;


public enum Algorithm {
    BCRYPT("BCRYPT") {
        @Override
        public HashProvider hashProvider() {
            return new BCryptProvider();
        }
    },
    PBKDF2WithHmacSHA1("PBKDF2WithHmacSHA1") {
        @Override
        public HashProvider hashProvider() {
            return new PBKDF2Provider();
        }
    },
    PBKDF2WithHmacSHA256("PBKDF2WithHmacSHA256") {
        @Override
        public HashProvider hashProvider() {
            return new PBKDF2Provider();
        }
    };

    private final String name;

    Algorithm(String name) {
        this.name = name;
    }

    public abstract HashProvider hashProvider();

    public String getName() {
        return name;
    }
}
