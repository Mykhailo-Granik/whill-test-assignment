package com.whill.assignment.repository;

import com.whill.assignment.entity.Match;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InMemoryMatchRepository implements MatchRepository {

    private static InMemoryMatchRepository instance;

    private final Map<String, Match> matches = new ConcurrentHashMap<>();

    private InMemoryMatchRepository() {
    }

    public static synchronized InMemoryMatchRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryMatchRepository();
        }
        return instance;
    }

    @Override
    public Match findById(String id) {
        return matches.get(id);
    }


    @Override
    public boolean upsertAtomically(Match match, Predicate<Match> upsertCondition) {
        Match computedMatch = matches.compute(
                match.getId(),
                (id, recorded) -> upsertCondition.test(recorded) ? match : recorded
        );
        if (computedMatch == match) {
            log.info("Persisting new information for match with id {}", match.getId());
            return true;
        }
        log.info("Not persisting information for match with id {} since storage has newer update",
                match.getId());
        return false;
    }

    @Override
    public void clear() {
        log.info("Clearing the storage");
        matches.clear();
    }
}
