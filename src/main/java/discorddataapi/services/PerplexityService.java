package discorddataapi.services;

import discorddataapi.configuration.PerplexityConfig;
import discorddataapi.models.pojo.PerplexityResponse;
import discorddataapi.models.pojo.PerplexityResult;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PerplexityService {

    private final PerplexityConfig config;
    private final RestTemplate restTemplate;

    public PerplexityService(PerplexityConfig config) {
        this.config = config;
        this.restTemplate = new RestTemplate();
    }

    public PerplexityResult askPerplexity(String prompt) {
        String url = config.getApiUrl();

        Map<String, Object> body = new HashMap<>();
        body.put("model", "sonar");
        body.put("max_output_tokens", 200);

        List<Map<String, String>> messages = List.of(
                Map.of(
                        "role", "system",
                        "content", "You are a helpful AI assistant in a Discord channel called cake. Your name is cakebot. It is very important to not be formal at all, to have fun. R rated humor is fun and encouraged. \n\nRules:\n1. Provide only the final answer. It is important that you do not include any explanation on the steps below.\n2. Do not show the intermediate steps information.\n\nSteps:\n1. Decide if the answer should be a brief sentence or a list of suggestions.\n2. If it is a list of suggestions, first, write a brief and natural introduction based on the original query.\n3. Followed by a list of suggestions, each suggestion should be split by two newlines. \n If you are not able to get search results or find relevant information, \n" +
                                "please state that clearly rather than providing speculative information. If no recent information is found, please indicate that no recent updates were discovered. Only provide information that you can verify from your search results, \n" +
                                "and clearly state if certain details are not available."
                ),
                Map.of(
                        "role", "user",
                        "content", prompt
                )
        );

        body.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(config.getApiKey());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<PerplexityResponse> response = restTemplate.exchange(url, HttpMethod.POST, request, PerplexityResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            PerplexityResponse apiResponse = response.getBody();

            String answer = apiResponse.getChoices() != null && !apiResponse.getChoices().isEmpty()
                    ? apiResponse.getChoices().get(0).getMessage().getContent()
                    : "";

            List<PerplexityResponse.Source> sources =
                    apiResponse.getSearch_results() != null ? apiResponse.getSearch_results() : List.of();

            return new PerplexityResult(answer, sources);
        }

        throw new RuntimeException("Failed to call Perplexity API: " + response.getStatusCode());
    }
}
