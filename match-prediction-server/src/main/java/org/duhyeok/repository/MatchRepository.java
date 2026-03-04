package org.duhyeok.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.duhyeok.dto.MatchResponseDto;
import org.duhyeok.sql.MatchSql; // SQL 인터페이스 임포트

@Repository
public class MatchRepository {

    private final JdbcTemplate jdbcTemplate;

    // 롬복 없이 생성자 주입
    public MatchRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MatchResponseDto findByGameId(String gameId) {
        return jdbcTemplate.queryForObject(MatchSql.SELECT_LATEST_MATCH, (rs, rowNum) -> {
            
            String tA = rs.getString("team_a_name");
            String tB = rs.getString("team_b_name");
            int aPos = rs.getInt("team_a_pos_cnt");
            int bPos = rs.getInt("team_b_pos_cnt");

            int total = aPos + bPos;
            double prob = (total == 0) ? 50.0 : (double) aPos / total * 100;
            String winner = (aPos > bPos) ? tA : (aPos < bPos) ? tB : "Draw";

            return new MatchResponseDto(
                tA, 
                tB, 
                aPos, 
                bPos, 
                Math.round(prob * 10) / 10.0, 
                winner
            );
        }, gameId);
    }
}