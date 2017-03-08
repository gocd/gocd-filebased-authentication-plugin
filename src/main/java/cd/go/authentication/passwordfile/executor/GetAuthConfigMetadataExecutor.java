package cd.go.authentication.passwordfile.executor;

import cd.go.authentication.passwordfile.annotation.MetadataHelper;
import cd.go.authentication.passwordfile.annotation.ProfileMetadata;
import cd.go.authentication.passwordfile.model.Configuration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.List;

public class GetAuthConfigMetadataExecutor implements RequestExecutor {
    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public GoPluginApiResponse execute() throws Exception {
        final List<ProfileMetadata> authConfigMetadata = MetadataHelper.getMetadata(Configuration.class);
        return new DefaultGoPluginApiResponse(200, GSON.toJson(authConfigMetadata));
    }
}
