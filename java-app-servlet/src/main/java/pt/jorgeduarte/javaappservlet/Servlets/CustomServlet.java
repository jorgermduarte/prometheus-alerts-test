package pt.jorgeduarte.javaappservlet.Servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "CustomServlet", urlPatterns = "/myservlet/*")
public class CustomServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");

        var uri = request.getRequestURI();

        if (uri.contains("nice")) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Página não encontrada INTERNAL ERROR");
            return;
        }

        response.getWriter().println("<html><body>");
        response.getWriter().println("<h1>Olá, Servlet!</h1>");
        response.getWriter().println("</body></html>");
    }
}