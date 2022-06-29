package org.course;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/evening")
public class EveningServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var writer = resp.getWriter();
        writer.write("Good evening, " + resolveName(req, resp));
    }

    private String resolveName(HttpServletRequest req, HttpServletResponse resp) {
        Optional.ofNullable(req.getParameter("name"))
                        .ifPresent(name -> SessionUtils.setAttribute(req, resp, "name", name));

        return Optional.ofNullable(SessionUtils.getAttribute(req, "name"))
                .orElse("Buddy");
    }
}
