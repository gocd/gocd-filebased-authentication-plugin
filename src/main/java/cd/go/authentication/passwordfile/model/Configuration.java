package cd.go.authentication.passwordfile.model;

import cd.go.authentication.passwordfile.annotation.ProfileField;
import cd.go.authentication.passwordfile.utils.Util;
import cd.go.authentication.passwordfile.annotation.MetadataHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Configuration {

    @Expose
    @SerializedName("PasswordFilePath")
    @ProfileField(key = "PasswordFilePath", required = true, secure = false)
    private String passwordFilePath;

    public static Configuration fromJSON(String json) {
        return Util.GSON.fromJson(json, Configuration.class);
    }

    public String getPasswordFilePath() {
        return passwordFilePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Configuration that = (Configuration) o;

        return passwordFilePath != null ? passwordFilePath.equals(that.passwordFilePath) : that.passwordFilePath == null;
    }

    @Override
    public int hashCode() {
        return passwordFilePath != null ? passwordFilePath.hashCode() : 0;
    }

    public static List<Map<String, String>> validate(Map<String, String> configuration) {
        final List<Map<String, String>> validationResult = MetadataHelper.validate(Configuration.class, configuration);
        return validationResult;
    }
}
