package cd.go.authentication.passwordfile;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PasswordFileReader {

    public Properties read(String passwordFilePath) throws IOException {
        final Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(passwordFilePath);
            properties.load(inputStream);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return properties;
    }
}
