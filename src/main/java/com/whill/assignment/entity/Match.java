package com.whill.assignment.entity;

import com.whill.assignment.repository.MatchRepository;
import com.whill.assignment.subscriber.Context;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@EqualsAndHashCode
public class Match implements Context {
    private final String id;
    private final Team homeTeam;
    private final Team awayTeam;
    private final int homeTeamScore;
    private final int awayTeamScore;
    private final long updateTimeStamp;

    public Match(String id) {
        this.id = id;
        homeTeam = null;
        awayTeam = null;
        homeTeamScore = 0;
        awayTeamScore = 0;
        updateTimeStamp = 0;
    }

    public void upsert(MatchRepository repository) {
        repository.upsertAtomically(this, new UpsertMatchPredicate(this));
    }

    public boolean isNewer(Match recorded) {
        return recorded.updateTimeStamp < updateTimeStamp;
    }

    public Match find(MatchRepository repository) {
        return repository.findById(id);
    }

}
