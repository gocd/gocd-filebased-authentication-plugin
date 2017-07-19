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
import javax.xml.bind.DatatypeConverter;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

public class PBKDF2Matcher implements HashMatcher {

    @Override
    public boolean matches(String plainText, String hashed) {
        PBKDF2HashInfo hashInfo = new PBKDF2HashInfo(hashed);

        String generatedHash = generateHash(plainText, hashInfo);

        return equalsIgnoreCase(generatedHash, hashInfo.getHashedPassword());
    }

    private String generateHash(String plainText, PBKDF2HashInfo hashInfo) {
        try {
            PBEKeySpec keySpec = new PBEKeySpec(plainText.toCharArray(), hashInfo.getSalt().getBytes(), hashInfo.getIterations(), hashInfo.getKeyLength());
            SecretKeyFactory factory = SecretKeyFactory.getInstance(hashInfo.getAlgorithm().getName());

            return DatatypeConverter.printHexBinary(factory.generateSecret(keySpec).getEncoded());
        } catch (Exception e) {
            throw new AuthenticationException(e);
        }
    }
}
