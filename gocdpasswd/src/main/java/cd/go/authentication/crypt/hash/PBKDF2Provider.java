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

import cd.go.authentication.crypt.CliArguments;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.nio.charset.StandardCharsets;

import static java.lang.String.format;


public class PBKDF2Provider implements HashProvider {
    @Override
    public String hash(CliArguments cliArguments) {
        try {
            PBEKeySpec keySpec = new PBEKeySpec(cliArguments.password().toCharArray(), cliArguments.salt().getBytes(), cliArguments.iterations(), cliArguments.keyLength());
            SecretKeyFactory factory = SecretKeyFactory.getInstance(cliArguments.algorithm().getName());

            final String hashedPasswd = printHexBinary(factory.generateSecret(keySpec).getEncoded());

            return format("%s=$%s$%s$%s$%s$%s", cliArguments.username(), cliArguments.algorithm().getName(), cliArguments.iterations(), cliArguments.keyLength(), cliArguments.salt(), hashedPasswd);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    private String printHexBinary(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }
}
