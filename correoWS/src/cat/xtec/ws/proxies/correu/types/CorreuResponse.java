package cat.xtec.ws.proxies.correu.types;

public class CorreuResponse {

    public static final String STATUS_OK = "OK";
    public static final String STATUS_KO = "KO";
    private int correuId;
    private String status;
    private String errorMessage;
    private CorreuInfo correu;

    public CorreuResponse() {
        this.status = "OK";
    }

    public void setCorreuId(int correuId) {
        this.correuId = correuId;
    }

    public int getCorreuId() {
        return this.correuId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public boolean isOk() {
        return "OK".equals(this.status);
    }

    public void setCorreu(CorreuInfo correu) {
        this.correu = correu;
    }

    public CorreuInfo getCorreu() {
        return this.correu;
    }
}
