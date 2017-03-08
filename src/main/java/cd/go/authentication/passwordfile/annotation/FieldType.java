package cd.go.authentication.passwordfile.annotation;

public enum FieldType {
    STRING {
        @Override
        public String validate(String value) {
            return null;
        }
    },

    POSITIVE_DECIMAL {
        @Override
        public String validate(String value) {
            try {
                if (Long.parseLong(value) < 0) {
                    return "must be positive decimal";
                }
            } catch (Exception e) {
                return "must be positive decimal";
            }
            return null;
        }
    },

    NUMBER {
        @Override
        public String validate(String value) {
            try {
                if (Double.parseDouble(value) < 0) {
                    return "must be number";
                }
            } catch (Exception e) {
                return "must be number";
            }

            return null;
        }
    };

    public abstract String validate(String value);
}
