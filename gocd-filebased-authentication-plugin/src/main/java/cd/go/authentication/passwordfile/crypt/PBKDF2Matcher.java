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

import cd.go.authentication.passwordfile.exception.AuthenticationException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;

public class PBKDF2Matcher implements HashMatcher {

    @Override
    public boolean matches(String plainText, String hashed) {
        PBKDF2HashInfo hashInfo = new PBKDF2HashInfo(hashed);

        String generatedHash = generateHash(plainText, hashInfo);

        return generatedHash.equalsIgnoreCase(hashInfo.getHashedPassword());
    }

    private String generateHash(String plainText, PBKDF2HashInfo hashInfo) {
        try {
            PBEKeySpec keySpec = new PBEKeySpec(plainText.toCharArray(), hashInfo.getSalt().getBytes(), hashInfo.getIterations(), hashInfo.getKeyLength());
            SecretKeyFactory factory = SecretKeyFactory.getInstance(hashInfo.getAlgorithm().getName());

            return printHexBinary(factory.generateSecret(keySpec).getEncoded());
        } catch (Exception e) {
            throw new AuthenticationException(e);
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
