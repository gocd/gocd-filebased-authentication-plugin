package cd.go.authentication.passwordfile.model;

import cd.go.authentication.passwordfile.utils.Util;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Capabilities {
    @Expose
    @SerializedName("supported_auth_type")
    private final SupportedAuthType supportedAuthType;

    @Expose
    @SerializedName("can_search")
    private final boolean canSearch;

    @Expose
    @SerializedName("can_verify_connection")
    private final boolean canVerifyConnection;

    @Expose
    @SerializedName("can_authorize")
    private final boolean canAuthorize;


    public Capabilities(SupportedAuthType supportedAuthType, boolean canSearch, boolean canVerifyConnection, boolean canAuthorize) {
        this.supportedAuthType = supportedAuthType;
        this.canSearch = canSearch;
        this.canVerifyConnection = canVerifyConnection;
        this.canAuthorize = canAuthorize;
    }

    public static Capabilities fromJSON(String json) {
        return Util.GSON.fromJson(json, Capabilities.class);
    }

    public String toJSON() {
        return Util.GSON.toJson(this);
    }
}
