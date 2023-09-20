package pt.jorgeduarte.javaappservlet.Controllers;

import jdk.jshell.spi.ExecutionControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;

@Controller
public class HomeController {
    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "Hello World!";
    }

    @GetMapping("/error")
    @ResponseBody
    public ResponseEntity<String> errorGet() {
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Custom internal server error");
    }

    @GetMapping("/error2")
    @ResponseBody
    public ResponseEntity<String> errorGe2t() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("xpto");
    }


}
