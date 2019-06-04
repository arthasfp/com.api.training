package requests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

public class HttpRequests {

    public static final Logger logger = LoggerFactory.getLogger(HttpRequests.class);

    public String doRequestWithPayload(final String resourceUrl, final RequestType requestType,
                                       final Map<String, String> requestProperties, final String postData) {
        String result = "";
        try {
            URL url = new URL(resourceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestType.name());
            requestProperties.forEach(conn::setRequestProperty);
            conn.setDoOutput(true);
            this.sendData(conn, postData);
            int responsecode = conn.getResponseCode();
            InputStream output = conn.getInputStream();
            Scanner s = new Scanner(output).useDelimiter("\\A");
            result = s.hasNext() ? s.next() : "";
        } catch (IOException e) {
            logger.info("Failed while working with url connection. Error msg: {}.", e.getMessage());
        }
        return result;
    }

    public String doGetRequest(final String resourceUrl, final Map<String, String> requestProperties) {
        String result = "";
        try {
            URL url = new URL(resourceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            requestProperties.forEach(conn::setRequestProperty);
            InputStream output = conn.getInputStream();
            String enc = conn.getHeaderField("Content-Encoding");
            Scanner s = new Scanner(output).useDelimiter("\\A");
            result = s.hasNext() ? s.next() : "";
        } catch (IOException e) {
            logger.debug("Failed while working with url connection. Error msg: {}.", e.getMessage());
        }
        return result;
    }

    public String doGetRequestAndDecode(final String resourceUrl, final Map<String, String> requestProperties) {
        String result = "";
        try {
            URL url = new URL(resourceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            requestProperties.forEach(conn::setRequestProperty);
            InputStream output = new GZIPInputStream(conn.getInputStream());
            String enc = conn.getHeaderField("Content-Encoding");
            Scanner s = new Scanner(output).useDelimiter("\\A");
            result = s.hasNext() ? s.next() : "";
            return result;
        } catch (IOException e) {
            logger.debug("Failed while working with url connection. Error msg: {}.", e.getMessage());
        }
        return result;
    }

    private void sendData(HttpURLConnection con, String data) {
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(data);
            wr.flush();
        } catch (IOException ioException) {
            logger.error("IO exception on try to send data", ioException);
        }
    }

}
