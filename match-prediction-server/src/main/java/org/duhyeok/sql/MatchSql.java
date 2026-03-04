package org.duhyeok.sql;

public interface MatchSql {
    String SELECT_LATEST_MATCH = 
        "SELECT team_a_name, team_b_name, team_a_pos_cnt, team_b_pos_cnt " +
        "FROM game_predict WHERE game_id = ?";
}