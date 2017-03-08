package cd.go.authentication.passwordfile;

import com.thoughtworks.go.plugin.api.request.DefaultGoApiRequest;
import com.thoughtworks.go.plugin.api.request.GoApiRequest;

public class RequestMother {
    public static GoApiRequest requestWithCredentials(String username, String password) {
        final DefaultGoApiRequest defaultGoApiRequest = new DefaultGoApiRequest(null, null, null);
        defaultGoApiRequest.setRequestBody(requestJson(username, password));
        return defaultGoApiRequest;
    }

    private static String requestJson(String username, String password) {
        return String.format("{\n" +
                "  \"credentials\": {\n" +
                "    \"username\": \"%s\",\n" +
                "    \"password\": \"%s\"\n" +
                "  },\n" +
                "  \"auth_configs\": [\n" +
                "    {\n" +
                "      \"id\": \"file\",\n" +
                "      \"configuration\": {\n" +
                "        \"PasswordFilePath\": \"password/file/path\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}", username, password);
    }
}
