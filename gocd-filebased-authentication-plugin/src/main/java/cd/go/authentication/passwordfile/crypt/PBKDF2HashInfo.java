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

import cd.go.authentication.passwordfile.AlgorithmIdentifier;
import cd.go.authentication.passwordfile.exception.InvalidFormatException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.text.MessageFormat.format;

public class PBKDF2HashInfo {
    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\$(\\w+)");

    private int iterations;
    private int keyLength;
    private String salt;
    private String hashedPassword;
    private Algorithm algorithm;

    public PBKDF2HashInfo(String hash) {
        this(hash, new AlgorithmIdentifier());
    }

    PBKDF2HashInfo(String hash, AlgorithmIdentifier algorithmIdentifier) {
        this.algorithm = algorithmIdentifier.identify(hash);

        List<String> parts = toList(hash);

        if (parts.size() != 5) {
            throw new InvalidFormatException(format("Invalid hash format for {0}. Must be provided in $Algorithm$Iteration$KeyLength$Salt$Hash format.", algorithm));
        }

        this.iterations = getInt(parts.get(1), "Iteration");
        this.keyLength = getInt(parts.get(2), "KeyLength");
        this.salt = parts.get(3);
        this.hashedPassword = parts.get(4);
    }

    private List<String> toList(String hashed) {
        List<String> parts = new ArrayList<>();
        final Matcher matcher = SPLIT_PATTERN.matcher(hashed);
        while (matcher.find()) {
            parts.add(matcher.group(1));
        }
        return parts;
    }


    private int getInt(String value, String key) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new InvalidFormatException(format("{0} must be an integer. Invalid hash config for {1}", key, algorithm));
        }
    }

    public int getIterations() {
        return iterations;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public String getSalt() {
        return salt;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }
}
