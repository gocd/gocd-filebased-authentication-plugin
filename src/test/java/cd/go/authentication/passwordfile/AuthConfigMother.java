package cd.go.authentication.passwordfile;

import cd.go.authentication.passwordfile.model.AuthConfig;

public class AuthConfigMother {

    public static AuthConfig authConfigWith(String passwordFilePath) {
        return AuthConfig.fromJSON(authConfigJson(passwordFilePath));
    }

    private static String authConfigJson(String passwordFilePath) {
        return String.format("{\n" +
                "  \"id\": \"file\",\n" +
                "  \"configuration\": {\n" +
                "    \"PasswordFilePath\": \"%s\"\n" +
                "  }\n" +
                "}", passwordFilePath);
    }
}
