package testUtils.mockWebServer;

import java.io.IOException;

import okhttp3.mockwebserver.MockWebServer;

public class MockWebUtils {

    private MockWebUtils() {
    }

    public static MockWebServer getMockWebServer() {
        return mockWebServer;
    }

    private static MockWebServer mockWebServer;
    private static String mockWebServerUrl;

    public static String getMockWebServerUrl() {
        return mockWebServerUrl;
    }

    public static void startServer() throws IOException {
        if (mockWebServer == null) {
            mockWebServer = new MockWebServer();
            mockWebServer.start();
        }
        mockWebServerUrl = mockWebServer.url("/").url().toString();
    }


    public static void stopServer() throws IOException {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }


    }

}
