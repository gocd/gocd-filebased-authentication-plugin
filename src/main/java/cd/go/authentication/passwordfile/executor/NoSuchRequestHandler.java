package cd.go.authentication.passwordfile.executor;

public class NoSuchRequestHandler extends RuntimeException {
    public NoSuchRequestHandler(String message) {
        super(message);
    }
}
