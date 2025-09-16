package discorddataapi.models.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PerplexityResult {
    private String response;
    private List<PerplexityResponse.Source> sources;
}
