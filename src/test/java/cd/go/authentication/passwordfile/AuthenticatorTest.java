package cd.go.authentication.passwordfile;

import cd.go.authentication.passwordfile.exception.AuthenticationException;
import cd.go.authentication.passwordfile.model.AuthConfig;
import cd.go.authentication.passwordfile.model.Credentials;
import cd.go.authentication.passwordfile.model.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class AuthenticatorTest {

    private Authenticator authenticator;
    private PasswordFileReader passwordFileReader;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        passwordFileReader = mock(PasswordFileReader.class);
        authenticator = new Authenticator(passwordFileReader);
    }

    @Test
    public void shouldAuthenticate() throws Exception {
        Credentials credentials = new Credentials("username", "password");
        AuthConfig authConfig = AuthConfigMother.authConfigWith("/var/etc/password.properties");
        final Properties properties = new Properties();

        properties.put("username", "W6ph5Mm5Pz8GgiULbPgzG37mj9g=");
        when(passwordFileReader.read(authConfig.getConfiguration().getPasswordFilePath())).thenReturn(properties);

        final User user = authenticator.authenticate(credentials, Collections.singletonList(authConfig));

        assertThat(user, is(new User("username", null, null)));
    }

    @Test
    public void shouldAuthenticateUserWithPasswordHavingSHAPrefix() throws Exception {
        Credentials credentials = new Credentials("username", "password");
        AuthConfig authConfig = AuthConfigMother.authConfigWith("/var/etc/password.properties");

        final Properties properties = new Properties();
        properties.put("username", "{SHA}W6ph5Mm5Pz8GgiULbPgzG37mj9g=");
        when(passwordFileReader.read(authConfig.getConfiguration().getPasswordFilePath())).thenReturn(properties);

        final User user = authenticator.authenticate(credentials, Collections.singletonList(authConfig));

        assertThat(user, is(new User("username", null, null)));
    }

    @Test
    public void shouldErrorOutInCaseOfInvalidPassword() throws Exception {
        Credentials credentials = new Credentials("username", "password");
        AuthConfig authConfig = AuthConfigMother.authConfigWith("/var/etc/password.properties");

        final Properties properties = new Properties();
        properties.put("username", "invalid-password");
        when(passwordFileReader.read(authConfig.getConfiguration().getPasswordFilePath())).thenReturn(properties);

        thrown.expect(AuthenticationException.class);
        thrown.expectMessage("Unable to authenticate user, user not found or bad credentials");

        authenticator.authenticate(credentials, Collections.singletonList(authConfig));
    }

    @Test
    public void shouldErrorOutInCaseOfUserNotExistInPasswordFile() throws Exception {
        Credentials credentials = new Credentials("username", "password");
        AuthConfig authConfig = AuthConfigMother.authConfigWith("/var/etc/password.properties");

        final Properties properties = new Properties();
        when(passwordFileReader.read(authConfig.getConfiguration().getPasswordFilePath())).thenReturn(properties);

        thrown.expect(AuthenticationException.class);
        thrown.expectMessage("Unable to authenticate user, user not found or bad credentials");

        authenticator.authenticate(credentials, Collections.singletonList(authConfig));
    }

    @Test
    public void shouldAuthenticateUserInCaseOfMultipleAuthConfigs() throws Exception {
        Credentials credentials = new Credentials("username", "password");
        AuthConfig authConfig1 = AuthConfigMother.authConfigWith("/var/etc/password_1.properties");
        AuthConfig authConfig2 = AuthConfigMother.authConfigWith("/var/etc/password_2.properties");

        final Properties properties = new Properties();
        when(passwordFileReader.read(authConfig1.getConfiguration().getPasswordFilePath())).thenReturn(properties);

        final Properties validProperties = new Properties();
        validProperties.put("username", "W6ph5Mm5Pz8GgiULbPgzG37mj9g=");
        when(passwordFileReader.read(authConfig2.getConfiguration().getPasswordFilePath())).thenReturn(validProperties);

        authenticator.authenticate(credentials, Arrays.asList(authConfig1, authConfig2));
    }

    @Test
    public void shouldAuthenticateUserAgainstASingleAuthConfigInCaseOfMultipleAuthConfigs() throws Exception {
        Credentials credentials = new Credentials("username", "password");
        AuthConfig authConfig1 = AuthConfigMother.authConfigWith("/var/etc/password_1.properties");
        AuthConfig authConfig2 = AuthConfigMother.authConfigWith("/var/etc/password_2.properties");

        final Properties properties = new Properties();
        properties.put("username", "W6ph5Mm5Pz8GgiULbPgzG37mj9g=");
        when(passwordFileReader.read(authConfig1.getConfiguration().getPasswordFilePath())).thenReturn(properties);

        authenticator.authenticate(credentials, Arrays.asList(authConfig1, authConfig2));

        verify(passwordFileReader, times(0)).read(authConfig2.getConfiguration().getPasswordFilePath());
    }
}