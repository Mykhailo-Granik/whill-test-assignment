package com.whill.assignment.util;

import com.whill.assignment.entity.Match;
import com.whill.assignment.entity.Team;

public class TestingMatchFactory {

    private static final String MATCH_ID = "id";
    private static final Team HOME_TEAM = new Team(
            "Dynamo",
            "Kyiv"
    );
    private static final Team AWAY_TEAM = new Team(
            "Shakhtar",
            "Donetsk"
    );
    private static final int HOME_TEAM_SCORE = 3;
    private static final int AWAY_TEAM_SCORE = 0;

    public static Match createMatch(long timestamp) {
        return new Match(
                MATCH_ID,
                HOME_TEAM,
                AWAY_TEAM,
                HOME_TEAM_SCORE,
                AWAY_TEAM_SCORE,
                timestamp
        );
    }

}
