package de.kaysubs.tracker.common;

import de.kaysubs.tracker.common.exception.HttpErrorCodeException;
import de.kaysubs.tracker.common.exception.HttpException;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.util.Timeout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class HttpUtil {
    public final static Timeout TIMEOUT = Timeout.ofMilliseconds(20000);

    public final static RequestConfig WITH_TIMEOUT = RequestConfig.custom()
            .setCookieSpec(StandardCookieSpec.RELAXED) // DEFAULT fails to parse cookies with a "expires" value
            .setConnectionRequestTimeout(TIMEOUT)

            .setConnectTimeout(TIMEOUT)
            .setResponseTimeout(TIMEOUT)
            .build();

    public static String readIntoString(ClassicHttpResponse response) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            return out.toString();
        } catch (IOException e) {
            throw new HttpException("Cannot read response content");
        }
    }

    public static void requireStatusCode(HttpResponse response, int code) {
        int statusCode = response.getCode();

        if (statusCode != code)
            throw new HttpErrorCodeException(statusCode);
    }

    public static HttpResponse executeRequest(HttpUriRequest request) {
        return executeRequest(request, HttpClients.createDefault());
    }

    public static HttpResponse executeRequest(HttpUriRequest request, Cookie[] cookies) {
        CookieStore store = new BasicCookieStore();
        Arrays.stream(cookies).forEach(store::addCookie);
        return HttpUtil.executeRequest(request, store);
    }

    public static HttpResponse executeRequest(HttpUriRequest request, CookieStore cookieStore) {
        CloseableHttpClient client = HttpClients.custom()
                .setDefaultCookieStore(cookieStore).build();

        return executeRequest(request, client);
    }

    public static HttpResponse executeRequest(HttpUriRequest request, HttpClient client) {
        try {
            return client.execute(request);
        } catch (IOException e) {
            throw new HttpException("Cannot Execute Http request", e);
        }
    }

}
