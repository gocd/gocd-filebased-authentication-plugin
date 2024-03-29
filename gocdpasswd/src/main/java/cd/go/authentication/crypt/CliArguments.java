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

package cd.go.authentication.crypt;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.util.Random;

public class CliArguments {
    private final PasswordReader passwordReader;

    CliArguments() {
        this(new PasswordReader());
    }

    CliArguments(PasswordReader passwordReader) {
        this.passwordReader = passwordReader;
    }

    @Parameter(names = {"-h", "help"}, description = "Print usage.", help = true)
    private boolean help;

    @Parameter(names = "-B",
            description = "Force bcrypt encryption of the password (very secure).")
    private boolean useBCrypt = false;

    @Parameter(names = "-P",
            description = "Force PBKDF2WithHmacSHA1 encryption of the password.")
    private boolean usePBKDF2WithHmacSHA1 = false;

    @Parameter(names = "-S",
            description = "Force PBKDF2WithHmacSHA256 encryption of the password.")
    private boolean usePBKDF2WithHmacSHA256 = false;

    @Parameter(names = "-u", description = "username", required = true, arity = 1)
    private String username;

    @Parameter(names = "-p", description = "Use the password from the command line rather than prompting for it.", arity = 1)
    private String password;

    //BCrypt options
    @Parameter(names = "-C",
            description = "Set the computing time used for the bcrypt useBCrypt(higher is more secure but slower, valid: 4 to 31).")
    private int cost = 5;

    // PBKDF2 options
    @Parameter(names = "-i",
            description = "Set the computing iterations used for PBKDF2WithHmacSHA1 and PBKDF2WithHmacSHA256.")
    private int iterations = 1000;

    @Parameter(names = "-l",
            description = "Set the key length(in bytes) used for PBKDF2WithHmacSHA1 and PBKDF2WithHmacSHA256.")
    private int keyLength = 32;

    @Parameter(names = "-s",
            description = "Cryptographic salt used for PBKDF2WithHmacSHA1 and PBKDF2WithHmacSHA256. It will be autogenerated if not provided.")
    private String salt;

    public Algorithm algorithm() {
        if (useBCrypt) {
            return Algorithm.BCRYPT;
        } else if (usePBKDF2WithHmacSHA1) {
            return Algorithm.PBKDF2WithHmacSHA1;
        } else if (usePBKDF2WithHmacSHA256) {
            return Algorithm.PBKDF2WithHmacSHA256;
        }

        throw new IllegalArgumentException("Expected encryption algorithm: [-B, -P, -S]");
    }

    public String username() {
        return username;
    }

    public String password() {
        if (isBlank(password)) {
            password = passwordReader.readPassword();
        }
        return password;
    }

    public int cost() {
        return cost;
    }

    public int iterations() {
        return iterations;
    }

    public int keyLength() {
        return keyLength * 8;
    }

    public String salt() {
        if (isBlank(salt)) {
            salt = getRandomHexString(32);
        }

        return salt;
    }

    public void validate() {
        if (help()) {
            return;
        }

        if (algorithm() == Algorithm.BCRYPT) {
            if (cost < 4 || cost > 31) {
                throw new IllegalArgumentException("Argument to -C must be an integer value: 4 to 31");
            }
        }
    }

    public static CliArguments fromArgs(String... args) {
        final CliArguments arguments = new CliArguments();

        JCommander.newBuilder()
                .addObject(arguments)
                .build().parse(args);

        arguments.validate();
        return arguments;
    }

    private String getRandomHexString(int sizeOfHex) {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while (sb.length() < sizeOfHex) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, sizeOfHex).toUpperCase();
    }

    public boolean help() {
        return this.help;
    }

    private boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

}
