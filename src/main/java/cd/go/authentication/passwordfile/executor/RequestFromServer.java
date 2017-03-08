package cd.go.authentication.passwordfile.executor;

public enum RequestFromServer {

    REQUEST_GET_PLUGIN_ICON(Constants.REQUEST_PREFIX + ".get-icon"),
    REQUEST_GET_CAPABILITIES(Constants.REQUEST_PREFIX + ".get-capabilities"),

    REQUEST_GET_AUTH_CONFIG_METADATA(String.join(".", Constants.REQUEST_PREFIX, Constants._AUTH_CONFIG_METADATA, "get-metadata")),
    REQUEST_AUTH_CONFIG_VIEW(String.join(".", Constants.REQUEST_PREFIX, Constants._AUTH_CONFIG_METADATA, "get-view")),
    REQUEST_VALIDATE_AUTH_CONFIG(String.join(".", Constants.REQUEST_PREFIX, Constants._AUTH_CONFIG_METADATA, "validate")),

    REQUEST_AUTHENTICATE_USER(Constants.REQUEST_PREFIX + ".authenticate-user"),
    REQUEST_SEARCH_USERS(Constants.REQUEST_PREFIX + ".search-users");

    private final String requestName;

    RequestFromServer(String requestName) {
        this.requestName = requestName;
    }

    public static RequestFromServer fromString(String requestName) {
        if (requestName != null) {
            for (RequestFromServer requestFromServer : RequestFromServer.values()) {
                if (requestName.equalsIgnoreCase(requestFromServer.requestName)) {
                    return requestFromServer;
                }
            }
        }

        throw new NoSuchRequestHandler("Request " + requestName + " is not supported by plugin.");
    }

    public String requestName() {
        return requestName;
    }

    private interface Constants {
        String REQUEST_PREFIX = "go.cd.authorization";
        String _AUTH_CONFIG_METADATA = "auth-config";
        String _ROLE_CONFIG_METADATA = "role-config";
    }
}

