package org.course;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class SessionUtils {
    private final String JSESSIONID = "JSESSIONID";
    private final Map<String, Map<String, String>> SESSION_MAP = new ConcurrentHashMap<>();


    public String getAttribute(HttpServletRequest req, String name) {
        return SESSION_MAP.getOrDefault(getSessionId(req), new HashMap<>()).get(name);
    }

    public void setAttribute(HttpServletRequest req, HttpServletResponse resp, String name, String val) {
        String sessionId = getSessionId(req);
        SESSION_MAP.computeIfAbsent(sessionId, id -> new HashMap<>())
                .put(name, val);

        resp.addCookie(new Cookie(JSESSIONID, sessionId));
    }

    private String getSessionId(HttpServletRequest req) {
        return findCookieByName(req.getCookies())
                .map(Cookie::getValue)
                .orElse(UUID.randomUUID().toString());
    }

    private Optional<Cookie> findCookieByName(Cookie[] cookies) {
        if (cookies == null) {
            return Optional.empty();
        } else {
            return Arrays.stream(cookies)
                    .filter(c -> c.getName().equalsIgnoreCase(JSESSIONID))
                    .findAny();
        }
    }
}
