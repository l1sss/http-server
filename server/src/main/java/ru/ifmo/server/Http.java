package ru.ifmo.server;

/**
 * HTTP constants.
 */

public class Http {
    public static final int SC_CONTINUE = 100;
    public static final int SC_OK = 200;
    public static final int SC_MULTIPLE_CHOICES = 300;
    public static final int SC_BAD_REQUEST = 400;
    public static final int SC_NOT_FOUND = 404;
    public static final int SC_SERVER_ERROR = 500;
    public static final int SC_NOT_IMPLEMENTED = 501;

    public static final char CONTENT_TYPE_SEPARATOR = '/';

    /**
     * OK header that preceded rest response data.
     */
    public static final String OK_HEADER = "HTTP/1.0 200 OK\r\n\r\n";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";

    /**
     * Constants for MIME
     */

    public static final String MIME_PNG = "image/png";
    public static final String MIME_JPEG = "image/jpeg";
    public static final String MIME_GIF = "image/gif";
    public static final String MIME_HTML = "text/html";
    public static final String MIME_TXT = "text/txt";
    public static final String MIME_PDF = "application/pdf";
    public static final String MIME_CSS = "text/css";
    public static final String MIME_JS = "application/javascript";
    public static final String MIME_MSWORD = "application/msword";
    public static final String MIME_MSEXCEL = "application/msexcel";
    public static final String MIME_BINARY = "application/octet-stream";
}
