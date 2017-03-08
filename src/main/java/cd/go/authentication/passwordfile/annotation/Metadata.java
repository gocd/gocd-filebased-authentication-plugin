package cd.go.authentication.passwordfile.annotation;

public interface Metadata {
    boolean isRequired();

    boolean isSecure();

    FieldType getType();
}
