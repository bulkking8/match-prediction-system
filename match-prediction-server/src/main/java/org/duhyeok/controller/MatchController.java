package org.duhyeok.controller;



import org.duhyeok.dto.MatchResponseDto;
import org.duhyeok.service.MatchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/predict")
public class MatchController {

    private final MatchService matchService;

    // 생성자 주입 (Lombok @RequiredArgsConstructor 대체)
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/{gameId}")
    public MatchResponseDto getMatchStats(@PathVariable String gameId) {
        return matchService.getPrediction(gameId);
    }
}