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
@RequestMapping("/api/v1/exp")
public class ExpController {
    private final ExpEventRepository expEventRepository;

    public ExpController(ExpEventRepository expEventRepository) {
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
    public List<ExpEvent> getExpEvents(
            @RequestParam String userId,
            @RequestParam(required = false) String reason,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        if (from == null) {
            if (reason != null) {
                return expEventRepository.findByUserIdAndReasonAndTimestampLessThanEqual(userId, reason, to);
            } else {
                return expEventRepository.findByUserIdAndTimestampLessThanEqual(userId, to);
            }
        }

        if (reason == null) {
            return expEventRepository.findByUserIdAndTimestampBetween(userId, from, to);
        }
        return expEventRepository.findByUserIdAndReasonAndTimestampBetween(userId, reason, from, to);
    }
}
