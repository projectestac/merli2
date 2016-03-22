package cat.xtec.ws.proxies.correu.types;

public class CorreuBody {

    public static final int TXT = 0;
    public static final int HTML = 1;
    private static final String[] bodyTypes = {"text/plain", "text/html"};
    private String content = "";
    private int bodyType = 0;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setBodyType(int bodyType)
            throws CorreuException {
        if ((bodyType < 0) || (bodyType >= bodyTypes.length)) {
            throw new CorreuException("Tipus de contingut no suportat: " + bodyType);
        }
        this.bodyType = bodyType;
    }

    public int getBodyType() {
        return this.bodyType;
    }

    public String getStringBodyType() {
        return bodyTypes[this.bodyType];
    }

    public String[] getSupportedBodyTypes() {
        return bodyTypes;
    }
}
