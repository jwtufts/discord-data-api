package discorddataapi.controllers;

import discorddataapi.models.pojo.PerplexityResult;
import discorddataapi.services.PerplexityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/perplexity")
public class PerplexityController {

    private final PerplexityService perplexityService;

    public PerplexityController(PerplexityService perplexityService) {
        this.perplexityService = perplexityService;
    }

    @GetMapping("/ask")
    public PerplexityResult ask(@RequestParam String question) {
        System.out.println("Asked Perplexity.");
        return perplexityService.askPerplexity(question);
    }
}
