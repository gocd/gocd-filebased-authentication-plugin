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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlgorithmIdentifier {
    private static final Pattern PREFIX_PATTERN = Pattern.compile("\\$(\\w+)");
    private static final Map<Algorithm, List<String>> ALGORITHM_IDENTIFIER_PREFIXS = new HashMap() {{
        put(Algorithm.BCRYPT, Arrays.asList("2A", "2Y", "2B"));
        put(Algorithm.PBKDF2WithHmacSHA1, Arrays.asList("PBKDF2WithHmacSHA1"));
        put(Algorithm.PBKDF2WithHmacSHA256, Arrays.asList("PBKDF2WithHmacSHA256"));
    }};

    public Algorithm identify(String hash) {
        if (hash.startsWith("{SHA1}") || !hash.startsWith("$")) {
            return Algorithm.SHA1;
        } else {
            final Matcher matcher = PREFIX_PATTERN.matcher(hash);

            if (matcher.find()) {
                String algorithmPrefix = matcher.group(1);

                return Arrays.stream(Algorithm.values())
                        .filter(elem -> {
                            List<String> strings = ALGORITHM_IDENTIFIER_PREFIXS.get(elem);
                            return strings != null && strings.stream().anyMatch(algorithmPrefix::equalsIgnoreCase);
                        })
                        .findFirst()
                        .orElseThrow(() -> new NoSuchAlgorithmException("Failed to determine hashing algorithm."));
            }

            throw new NoSuchAlgorithmException("Failed to determine hashing algorithm.");
        }
    }
}
