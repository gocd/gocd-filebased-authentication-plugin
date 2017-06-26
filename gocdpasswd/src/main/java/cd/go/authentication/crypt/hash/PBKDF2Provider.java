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

package cd.go.authentication.crypt.hash;

import cd.go.authentication.crypt.CliArguments;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;

import static java.lang.String.format;


public class PBKDF2Provider implements HashProvider {
    @Override
    public String hash(CliArguments cliArguments) {
        try {
            PBEKeySpec keySpec = new PBEKeySpec(cliArguments.password().toCharArray(), cliArguments.salt().getBytes(), cliArguments.iterations(), cliArguments.keyLength());
            SecretKeyFactory factory = SecretKeyFactory.getInstance(cliArguments.algorithm().getName());

            final String hashedPasswd = toHex(factory.generateSecret(keySpec).getEncoded());

            return format("%s=$%s$%s$%s$%s$%s", cliArguments.username(), cliArguments.algorithm().getName(), cliArguments.iterations(), cliArguments.keyLength(), cliArguments.salt(), hashedPasswd);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
}
