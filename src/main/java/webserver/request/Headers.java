package webserver.request;

import utils.CookieUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Headers {

    public static final String COOKIE_KEY_NAME = "Cookie";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String LOCATION = "Location";

    private final Map<String, String> headerMap;

    public Headers(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public static Headers empty() {
        return new Headers(new HashMap<>());
    }

    public void addHeaderByLine(String line) {
        String[] split = line.split(": ", 2);
        if (split.length < 2) {
            return;
        }

        headerMap.put(split[0], split[1]);
    }

    public String get(String s) {
        return headerMap.get(s);
    }

    public int getContentLength() {
        String contentLength = headerMap.get(CONTENT_LENGTH);
        try {
            return Integer.parseInt(contentLength);
        } catch (Exception e) {
        }
        return 0;
    }

    public void put(String key, String value) {
        headerMap.put(key, value);
    }

    public void putCookie(String key, String value) {
        Map<String, String> cookies = CookieUtils.strToCookieMap(headerMap.get(COOKIE_KEY_NAME));
        cookies.put(key, value);
        String cookieMapStr = CookieUtils.cookieMapToStr(cookies);
        headerMap.put(COOKIE_KEY_NAME, cookieMapStr);
    }

    public void setTextHtml() {
        headerMap.put(CONTENT_TYPE, "text/html; charset=utf-8");
    }

    public void setTextCss() {
        headerMap.put(CONTENT_TYPE, "text/css");
    }

    public String response() {
        return headerMap.entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey();
                    if (key == "Cookie") {
                        key = "Set-Cookie";
                    }
                    return key + ": " + entry.getValue();
                })
                .collect(Collectors.joining("\r\n")) + "\r\n";
    }

    public void setContentLength(int length) {
        headerMap.put(CONTENT_LENGTH, String.valueOf(length));
    }
}
