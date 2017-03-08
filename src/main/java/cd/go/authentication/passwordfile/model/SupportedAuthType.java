package cd.go.authentication.passwordfile.model;

import com.google.gson.annotations.SerializedName;

public enum SupportedAuthType {
    @SerializedName("password")
    Password,
    @SerializedName("web")
    Web
}
