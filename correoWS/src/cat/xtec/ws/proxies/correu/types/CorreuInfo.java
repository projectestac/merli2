package cat.xtec.ws.proxies.correu.types;

import java.util.ArrayList;

public class CorreuInfo {

    private String from;
    private ArrayList replyAddresses = new ArrayList();
    private ArrayList tos = new ArrayList();
    private ArrayList ccs = new ArrayList();
    private ArrayList bccs = new ArrayList();
    private String subject;
    private CorreuBody body = new CorreuBody();
    private ArrayList attachments = new ArrayList();

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return this.from;
    }

    public ArrayList getReplyAddresses() {
        return this.replyAddresses;
    }

    public ArrayList getTos() {
        return this.tos;
    }

    public ArrayList getCcs() {
        return this.ccs;
    }

    public ArrayList getBccs() {
        return this.bccs;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return this.subject;
    }

    public CorreuBody getBody() {
        return this.body;
    }

    public ArrayList getAttachments() {
        return this.attachments;
    }

    public void addTo(String tos) {
        if ((tos == null) || (tos.equals(""))) {
            return;
        }
        String[] addresses = tos.split(";");
        for (int i = 0; i < addresses.length; i++) {
            this.tos.add(addresses[i]);
        }
    }

    public void addCc(String ccs) {
        if ((ccs == null) || (ccs.equals(""))) {
            return;
        }
        String[] addresses = ccs.split(";");
        for (int i = 0; i < addresses.length; i++) {
            this.ccs.add(addresses[i]);
        }
    }

    public void addBcc(String bccs) {
        if ((bccs == null) || (bccs.equals(""))) {
            return;
        }
        String[] addresses = bccs.split(";");
        for (int i = 0; i < addresses.length; i++) {
            this.bccs.add(addresses[i]);
        }
    }

    public void setBodyInfo(int bodyType, String bodyContent)
            throws CorreuException {
        this.body.setBodyType(bodyType);
        this.body.setContent(bodyContent);
    }

    public void addAttachment(byte[] attachment, String attachmentName, String attachmentType) {
        CorreuAttachment att = new CorreuAttachment();
        att.setContent(attachment);
        att.setFileName(attachmentName);
        att.setFileType(attachmentType);
        this.attachments.add(att);
    }
}
