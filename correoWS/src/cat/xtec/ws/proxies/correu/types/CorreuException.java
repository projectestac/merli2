package cat.xtec.ws.proxies.correu.types;

public class CorreuException
        extends Exception {

    public CorreuException(String message) {
        super(message);
    }

    public CorreuException(String message, Throwable ex) {
        super(message, ex);
    }
}
