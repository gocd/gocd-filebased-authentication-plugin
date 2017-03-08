package cd.go.authentication.passwordfile.annotation;


import java.lang.reflect.Field;
import java.util.*;

public class MetadataHelper {

    public static List<ProfileMetadata> getMetadata(Class<?> clazz) {
        return buildMetadata(clazz);
    }

    private static List<ProfileMetadata> buildMetadata(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<ProfileMetadata> metadata = new ArrayList<>();
        for (Field field : fields) {
            ProfileField profileField = field.getAnnotation(ProfileField.class);
            if (profileField != null) {
                final FieldMetadata fieldMetadata = new FieldMetadata(profileField.required(), profileField.secure(), profileField.type());
                final ProfileMetadata<FieldMetadata> profileMetadata = new ProfileMetadata<>(profileField.key(), fieldMetadata);
                metadata.add(profileMetadata);
            }
        }
        return metadata;
    }

    public static List<Map<String, String>> validate(Class<?> clazz, Map<String, String> configuration) {
        List<Map<String, String>> result = new ArrayList<>();
        List<String> knownFields = new ArrayList<>();

        for (ProfileMetadata field : getMetadata(clazz)) {
            knownFields.add(field.getKey());

            Map<String, String> validationError = field.validate(configuration.get(field.getKey()));

            if (!validationError.isEmpty()) {
                result.add(validationError);
            }
        }


        Set<String> set = new HashSet<>(configuration.keySet());
        set.removeAll(knownFields);

        if (!set.isEmpty()) {
            for (String key : set) {
                LinkedHashMap<String, String> validationError = new LinkedHashMap<>();
                validationError.put("key", key);
                validationError.put("message", "Is an unknown property");
                result.add(validationError);
            }
        }
        return result;
    }
}
