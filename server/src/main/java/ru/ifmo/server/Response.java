package ru.ifmo.server;

import javax.xml.soap.MimeHeaders;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Provides {@link java.io.OutputStream} ro respond to client.
 */
public class Response {
    final Socket socket;
    public Object location;



    Response(Socket socket) {
        this.socket = socket;
    }

    /**
     * @return {@link OutputStream} connected to the client.
     */
    public OutputStream getOutputStream() {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
            throw new ServerException("Cannot get output stream", e);
        }
    }

    public void setCharSet() {

    }

    public void sendRedirect(String location) throws IOException {
        this.location = location;



    }


    public void forward(String resource) {

    }
}