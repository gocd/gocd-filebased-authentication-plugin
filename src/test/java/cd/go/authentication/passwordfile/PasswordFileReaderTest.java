package cd.go.authentication.passwordfile;

import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PasswordFileReaderTest {

    @Test
    public void shouldReadPasswordFile() throws Exception {
        PasswordFileReader reader = new PasswordFileReader();
        Properties properties = reader.read(PasswordFileReaderTest.class.getResource("/password.properties").getFile());

        assertThat(properties.getProperty("username"), is("{SHA}W6ph5Mm5Pz8GgiULbPgzG37mj9g="));
    }
}