package ru.ifmo.server;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

/**
 * Util methods for tests.
 */
public final class TestUtils {
    private TestUtils() {
    }

    public static String toString(InputStream in) throws IOException {
        InputStreamReader reader = new InputStreamReader(in);

        StringBuilder sb = new StringBuilder();
        char[] buf = new char[1024];

        int len;

        while ((len = reader.read(buf)) > 0) {
            sb.append(buf, 0, len);

            if (len < buf.length)
                break;
        }

        return sb.toString();
    }

    public static void assertStatusCode(int expected, HttpResponse response) {
        assertEquals("Wrong status code received", expected, response.getStatusLine().getStatusCode());
    }


    public static class HeaderFilter extends Filter {

        String name = null;
        int x = 1;

        public HeaderFilter(String name, int x) {
            this.name = name;
            this.x = x;
        }

        @Override
        public void doFilter(Request request, Response response) throws IOException {

            request.addHeader(name, String.valueOf(x));

            nextFilter.doFilter(request, response);

        }
    }
}
