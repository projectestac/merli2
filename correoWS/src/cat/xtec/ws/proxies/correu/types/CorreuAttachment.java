package cat.xtec.ws.proxies.correu.types;

public class CorreuAttachment {

    private byte[] content = null;
    private String fileName = null;
    private String fileType = null;

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return this.content;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileType() {
        return this.fileType;
    }
}
