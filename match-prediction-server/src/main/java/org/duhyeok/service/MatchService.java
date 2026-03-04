package org.duhyeok.service;

import org.duhyeok.dto.MatchResponseDto;
import org.duhyeok.repository.MatchRepository;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public MatchResponseDto getPrediction(String gameId) {
        // Repository에서 데이터를 가져와서 분석 로직 실행
        return matchRepository.findByGameId(gameId);
    }
}