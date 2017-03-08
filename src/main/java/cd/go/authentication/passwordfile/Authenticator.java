package cd.go.authentication.passwordfile;

import cd.go.authentication.passwordfile.exception.AuthenticationException;
import cd.go.authentication.passwordfile.model.AuthConfig;
import cd.go.authentication.passwordfile.model.Credentials;
import cd.go.authentication.passwordfile.model.User;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

public class Authenticator {
    final PasswordFileReader passwordFileReader;

    public Authenticator() {
        this(new PasswordFileReader());
    }

    public Authenticator(PasswordFileReader passwordFileReader) {
        this.passwordFileReader = passwordFileReader;
    }

    public User authenticate(Credentials credentials, List<AuthConfig> authConfigs) throws IOException, NoSuchAlgorithmException {

        for (AuthConfig authConfig : authConfigs) {
            final String passwordFilePath = authConfig.getConfiguration().getPasswordFilePath();
            final Properties usernamePassword = passwordFileReader.read(passwordFilePath);

            if (!usernamePassword.containsKey(credentials.getUsername())) {
                continue;
            }

            final String password = stripShaFromPasswordIfRequired(usernamePassword.getProperty(credentials.getUsername()));

            if (password.equals(sha1Digest(credentials.getPassword().getBytes()))) {
                return new User(credentials.getUsername(), null, null);
            }
        }

        throw new AuthenticationException("Unable to authenticate user, user not found or bad credentials");
    }

    private String stripShaFromPasswordIfRequired(String password) {
        if (password.startsWith("{SHA}")) {
            return password.substring(5);
        }
        return password;
    }

    private String sha1Digest(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        return Base64.getEncoder().encodeToString(md.digest(bytes));
    }
}