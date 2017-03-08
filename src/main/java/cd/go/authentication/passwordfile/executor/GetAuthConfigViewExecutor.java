package cd.go.authentication.passwordfile.executor;

import cd.go.authentication.passwordfile.utils.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public class GetAuthConfigViewExecutor implements RequestExecutor {
    private static final Gson GSON = new Gson();

    @Override
    public GoPluginApiResponse execute() throws Exception {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("template", Util.readResource("/auth-config.template.html"));
        DefaultGoPluginApiResponse defaultGoPluginApiResponse = new DefaultGoPluginApiResponse(200, GSON.toJson(jsonObject));
        return defaultGoPluginApiResponse;
    }

}
