package cat.xtec.ws.proxies.correu.types;

import java.util.ArrayList;

public class EnviamentResponse {

    public static final String STATUS_OK = "OK";
    public static final String STATUS_KO = "KO";
    private String status;
    private String message;
    private ArrayList respostesCorreus = new ArrayList();
    private ArrayList unsendedMessages = new ArrayList();

    public EnviamentResponse() {
        this.status = "OK";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void addCorreuResponse(CorreuResponse cResp) {
        this.respostesCorreus.add(cResp);
        if (!cResp.isOk()) {
            this.unsendedMessages.add(cResp);
        }
    }

    public ArrayList getRespostesCorreu() {
        return this.respostesCorreus;
    }

    public boolean isOk() {
        return "OK".equals(this.status);
    }

    public ArrayList unsendedMessages() {
        return this.unsendedMessages;
    }
}
