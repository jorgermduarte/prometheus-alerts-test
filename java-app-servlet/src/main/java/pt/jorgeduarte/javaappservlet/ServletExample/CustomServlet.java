package pt.jorgeduarte.javaappservlet.ServletExample;

import io.prometheus.client.hotspot.DefaultExports;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "CustomServlet", urlPatterns = "/myservlet")
public class CustomServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        DefaultExports.initialize();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        response.getWriter().println("<html><body>");
        response.getWriter().println("<h1>Olá, Servlet!</h1>");
        response.getWriter().println("</body></html>");
    }
}
