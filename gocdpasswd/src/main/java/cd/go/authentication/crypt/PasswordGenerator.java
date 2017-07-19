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

import com.beust.jcommander.JCommander;

import java.io.File;

import static java.lang.String.format;

public class PasswordGenerator {

    public static void main(String[] args) {
        new PasswordGenerator().generate(args);
    }

    public void generate(String... args) {
        try {
            CliArguments cliArguments = CliArguments.fromArgs(args);

            if (cliArguments.help()) {
                printUsageAndExit(0, cliArguments);
            }

            final String hash = cliArguments.algorithm().hashProvider().hash(cliArguments);

            printResult(hash);

        } catch (Exception e) {
            System.err.println(format("\n%s", e.getMessage()));
            printUsageAndExit(1, new CliArguments());
        }
    }

    void printResult(String hash) {
        System.out.println(hash);
    }

    void printUsageAndExit(int exitCode, CliArguments cliArguments) {
        StringBuilder out = new StringBuilder();

        JCommander jCommander = new JCommander(cliArguments);
        jCommander.setProgramName("java -jar " + jarName());
        jCommander.usage(out);
        System.err.println(out);
        System.exit(exitCode);
    }

    private String jarName() {
        return new File(getClass().getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
    }
}
