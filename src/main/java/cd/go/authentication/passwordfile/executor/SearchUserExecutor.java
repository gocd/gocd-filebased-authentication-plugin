package cd.go.authentication.passwordfile.executor;

import cd.go.authentication.passwordfile.PasswordFileReader;
import cd.go.authentication.passwordfile.model.AuthConfig;
import cd.go.authentication.passwordfile.utils.Util;
import cd.go.authentication.passwordfile.model.User;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.io.IOException;
import java.util.*;

public class SearchUserExecutor implements RequestExecutor {
    public static final String SEARCH_TERM = "search_term";
    private final GoPluginApiRequest request;
    private PasswordFileReader passwordFileReader;

    public SearchUserExecutor(GoPluginApiRequest request) {
        this(request, new PasswordFileReader());
    }

    protected SearchUserExecutor(GoPluginApiRequest request, PasswordFileReader passwordFileReader) {
        this.request = request;
        this.passwordFileReader = passwordFileReader;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        Map<String, String> requestParam = Util.GSON.fromJson(request.requestBody(), Map.class);
        String searchTerm = requestParam.get(SEARCH_TERM);
        List<AuthConfig> authConfigs = AuthConfig.fromJSONList(request.requestBody());

        final Set<User> users = searchUsers(searchTerm, authConfigs);

        return new DefaultGoPluginApiResponse(200, Util.GSON.toJson(users));
    }

    Set<User> searchUsers(String searchTerm, List<AuthConfig> authConfigs) throws IOException {
        final HashSet<User> users = new HashSet<>();
        for (AuthConfig authConfig: authConfigs) {
            users.addAll(search(searchTerm, authConfig));
        }
        return users;
    }

    public Set<User> search(String searchText, AuthConfig authConfig) throws IOException {
        final Properties properties = passwordFileReader.read(authConfig.getConfiguration().getPasswordFilePath());
        Set<User> users = findUserNameContaining(searchText, properties);
        return users;
    }

    private Set<User> findUserNameContaining(String searchText, Properties properties) {
        Set<User> users = new HashSet<>();
        for (Object o : properties.keySet()) {
            String username = (String) o;
            if (username.toLowerCase().contains(searchText.toLowerCase())) {
                users.add(new User(username, null, null));
            }
        }
        return users;
    }

}
