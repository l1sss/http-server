package ru.ifmo.server;

/**
 * Created by l1s on 19.05.17.
 */
public class Body {
    private String contentType;
    private String txtContent;
    private String contentFormat;
    private int contentLength;
    private byte[] binContent;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTxtContent() {
        return txtContent;
    }

    public void setTxtContent(String txtContent) {
        this.txtContent = txtContent;
    }

    public String getContentFormat() {
        return contentFormat;
    }

    public void setContentFormat(String contentFormat) {
        this.contentFormat = contentFormat;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getBinContent() {
        return binContent;
    }

    public void setBinContent(byte[] binContent) {
        this.binContent = binContent;
    }

    public boolean reasonToParse() {
        return contentType != null && contentLength != 0;
    }
}