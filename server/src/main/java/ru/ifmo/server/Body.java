package ru.ifmo.server;

import static ru.ifmo.server.Http.CONTENT_TYPE_SEPARATOR;

/**
 * Created by l1s on 19.05.17.
 */
public class Body {
    String contentType;
    String txtContent;
    int contentLength;
    byte[] binContent;

    public String getContentType() {
        return contentType;
    }

    public String getTxtContent() {
        return txtContent;
    }

    public String getContentFormat() {
        return parseFormat();
    }

    public int getContentLength() {
        return contentLength;
    }

    public byte[] getBinContent() {
        return binContent;
    }

    /**
     *
     * @return String content format
     */
    private String parseFormat() {
        if (contentType == null)
            return "bin";

        String format = null;

        int len = contentType.length();

        for (int i = 0; i < len; i++) {
            if (contentType.charAt(i) == CONTENT_TYPE_SEPARATOR)
                format = contentType.substring(i + 1, contentType.length()).trim();
        }

        return format;
    }

    boolean contentPresent() {
        return contentType != null && contentLength != 0;
    }
}