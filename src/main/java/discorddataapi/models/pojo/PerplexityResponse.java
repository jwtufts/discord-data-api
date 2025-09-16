package discorddataapi.models.pojo;

import lombok.Data;

import java.util.List;

@Data
public class PerplexityResponse {
    private String id;
    private String model;
    private List<Choice> choices;
    private List<Source> search_results;

    @Data
    public static class Choice {
        private int index;
        private Message message;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
        private List<Source> citations;
    }

    @Data
    public static class Source {
        private String title;
        private String url;
        private String date;
        private String last_updated;
    }
}
