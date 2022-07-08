package com.whill.assignment.repository;

import com.whill.assignment.entity.Match;

import java.util.function.Predicate;

public interface MatchRepository {

    Match findById(String id);

    /**
     * @return true if provided value was persisted, false otherwise
     */
    boolean upsertAtomically(Match match, Predicate<Match> upsertCondition);

    void clear();

}
