package discorddataapi.controllers;

import discorddataapi.models.Author;
import discorddataapi.services.ActiveUsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guild")
public class GuildController {
    private final ActiveUsersService activeUsersService;

    public GuildController(ActiveUsersService activeUsersService) {
        this.activeUsersService = activeUsersService;
    }

    @GetMapping("/allActiveUsers")
    public ResponseEntity<List<Author>> getAllActiveUsers() {
        return ResponseEntity.ok(activeUsersService.getAuthors());
    }
}
