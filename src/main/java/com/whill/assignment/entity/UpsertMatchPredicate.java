package com.whill.assignment.entity;

import java.util.function.Predicate;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpsertMatchPredicate implements Predicate<Match> {

    private final Match current;

    @Override
    public boolean test(Match recorded) {
        return upsertNeeded(recorded);
    }

    private boolean upsertNeeded(Match recorded) {
        return noMatchInfo(recorded) || currentUpdateIsNewer(recorded);
    }

    private boolean noMatchInfo(Match recorded) {
        return recorded == null;
    }

    private boolean currentUpdateIsNewer(Match recorded) {
        return current.isNewer(recorded);
    }

}
