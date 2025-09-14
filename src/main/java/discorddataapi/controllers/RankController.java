package discorddataapi.controllers;

import discorddataapi.models.UserExp;
import discorddataapi.models.pojo.UserRank;
import discorddataapi.repositories.leveling.UserExpRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/rank")
public class RankController {
    private final UserExpRepository userExpRepository;

    public RankController(UserExpRepository userExpRepository) {
        this.userExpRepository = userExpRepository;
    }

    @GetMapping("/byUserId")
    public UserRank getRank(@RequestParam String userId) {
        List<UserExp> userExps = userExpRepository.findAll();

        Function<Comparator<UserExp>, Integer> rankFor = comparator -> {
            List<UserExp> sorted = userExps.stream()
                    .sorted(comparator.reversed())
                    .toList();

            for (int i = 0; i < sorted.size(); i++) {
                if (sorted.get(i).getUserId().equals(userId)) {
                    return i + 1; // 1-based index
                }
            }

            return -1;
        };

        UserRank rank = new UserRank();
        rank.setDaily(rankFor.apply(Comparator.comparingInt(UserExp::getDaily)));
        rank.setWeekly(rankFor.apply(Comparator.comparingInt(UserExp::getWeekly)));
        rank.setMonthly(rankFor.apply(Comparator.comparingInt(UserExp::getMonthly)));
        rank.setYearly(rankFor.apply(Comparator.comparingInt(UserExp::getYearly)));
        rank.setTotal(rankFor.apply(Comparator.comparingInt(UserExp::getTotal)));
        rank.setUnboosted(rankFor.apply(Comparator.comparingInt(UserExp::getUnboosted)));

        return rank;
    }
}
