package cd.go.authentication.passwordfile;

import cd.go.authentication.passwordfile.executor.*;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import static com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse.INTERNAL_ERROR;

@Extension
public class PasswordFilePlugin implements GoPlugin {
    public static final Logger LOG = Logger.getLoggerFor(PasswordFilePlugin.class);

    private GoApplicationAccessor accessor;

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        try {
            switch (RequestFromServer.fromString(request.requestName())) {
                case REQUEST_GET_PLUGIN_ICON:
                    return new GetPluginIconExecutor().execute();
                case REQUEST_GET_CAPABILITIES:
                    return new GetCapabilitiesExecutor().execute();
                case REQUEST_GET_AUTH_CONFIG_METADATA:
                    return new GetAuthConfigMetadataExecutor().execute();
                case REQUEST_AUTH_CONFIG_VIEW:
                    return new GetAuthConfigViewExecutor().execute();
                case REQUEST_VALIDATE_AUTH_CONFIG:
                    return new AuthConfigValidateRequestExecutor(request).execute();
                case REQUEST_AUTHENTICATE_USER:
                    return new UserAuthenticationExecutor(request, new Authenticator()).execute();
                case REQUEST_SEARCH_USERS:
                    return new SearchUserExecutor(request).execute();
                default:
                    throw new UnhandledRequestTypeException(request.requestName());
            }
        } catch (NoSuchRequestHandler e) {
            LOG.warn(e.getMessage());
            return new DefaultGoPluginApiResponse(INTERNAL_ERROR, e.getMessage());
        } catch (Exception e) {
            LOG.error("Error while executing request " + request.requestName(), e);
            return new DefaultGoPluginApiResponse(INTERNAL_ERROR, e.getMessage());
        }
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return Constants.PLUGIN_IDENTIFIER;
    }
}
