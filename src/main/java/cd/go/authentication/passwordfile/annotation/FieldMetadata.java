package cd.go.authentication.passwordfile.annotation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FieldMetadata implements Metadata {

    @Expose
    @SerializedName("required")
    private boolean required;

    @Expose
    @SerializedName("secure")
    private boolean secure;

    private FieldType type;

    public FieldMetadata(boolean required, boolean secure, FieldType type) {
        this.required = required;
        this.secure = secure;
        this.type = type;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public FieldType getType() {
        return type;
    }
}
