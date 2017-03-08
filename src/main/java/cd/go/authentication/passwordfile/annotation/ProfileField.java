package cd.go.authentication.passwordfile.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProfileField {
    String key();

    boolean required();

    boolean secure();

    FieldType type() default FieldType.STRING;
}
