package discorddataapi.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class AuthController {
    @GetMapping("/auth/discord/login")
    public void redirectToDiscord(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/discord");
    }

    @GetMapping("/auth/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        new SecurityContextLogoutHandler().logout(request, null, null);

        response.sendRedirect("http://localhost:5173");
    }
}
