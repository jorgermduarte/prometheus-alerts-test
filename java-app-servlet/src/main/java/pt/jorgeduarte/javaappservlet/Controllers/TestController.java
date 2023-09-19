package pt.jorgeduarte.javaappservlet.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequestMapping("/report/odata4")
public class TestController {
    @RequestMapping("/**")
    @GetMapping
    @PostMapping
    public void genericRoute(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        try (final PrintWriter writer = resp.getWriter()) {
            writer.println("<html><body>");
            writer.println("<h1>Olá, Controller</h1>");
            writer.println("</body></html>");
        }
    }

}
