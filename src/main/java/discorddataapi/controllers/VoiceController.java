package discorddataapi.controllers;

import discorddataapi.models.ExpEvent;
import discorddataapi.repositories.leveling.ExpEventRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/voice")
public class VoiceController {
    private final ExpEventRepository expEventRepository;

    public VoiceController(ExpEventRepository expEventRepository) {
        this.expEventRepository = expEventRepository;
    }

    @GetMapping("/minutes/byUserId")
    public int getVoiceMinByUserAndDateRange(
            @RequestParam String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        List<ExpEvent> allVoiceEvents = expEventRepository.findByUserIdAndReasonAndTimestampBetween(userId, "voice", from, to);

        return allVoiceEvents.stream()
                .map(ExpEvent::getTime)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();
    }

    @GetMapping("/byUserId")
    public List<ExpEvent> getVoiceExpEvents(
            @RequestParam String userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        if (from == null) {
            return expEventRepository.findByUserIdAndReasonAndTimestampLessThanEqual(userId, "voice", to);
        }

        return expEventRepository.findByUserIdAndReasonAndTimestampBetween(userId, "voice", from, to);
    }
}
