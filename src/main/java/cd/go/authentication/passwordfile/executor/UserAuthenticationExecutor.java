package cd.go.authentication.passwordfile.executor;

import cd.go.authentication.passwordfile.Authenticator;
import cd.go.authentication.passwordfile.exception.AuthenticationException;
import cd.go.authentication.passwordfile.model.AuthConfig;
import cd.go.authentication.passwordfile.model.Credentials;
import cd.go.authentication.passwordfile.model.User;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse.SUCCESS_RESPONSE_CODE;

public class UserAuthenticationExecutor implements RequestExecutor {
    private static final Gson GSON = new Gson();
    private final GoPluginApiRequest request;
    private final Authenticator authenticator;

    public UserAuthenticationExecutor(GoPluginApiRequest request, Authenticator authenticator) {
        this.request = request;
        this.authenticator = authenticator;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        Credentials credentials = Credentials.fromJSON(request.requestBody());
        final List<AuthConfig> authConfigs = AuthConfig.fromJSONList(request.requestBody());

        Map<String, Object> userMap = new HashMap<>();

        try {
            final User user = authenticator.authenticate(credentials, authConfigs);
            userMap.put("user", user);
            userMap.put("roles", Collections.emptyList());

            return new DefaultGoPluginApiResponse(SUCCESS_RESPONSE_CODE, GSON.toJson(userMap));
        } catch (AuthenticationException e) {
            return new DefaultGoPluginApiResponse(SUCCESS_RESPONSE_CODE, GSON.toJson(userMap));
        }
    }
}
