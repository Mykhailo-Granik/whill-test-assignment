package com.whill.assignment.repository;

import com.whill.assignment.entity.Match;
import com.whill.assignment.subscriber.Subscriber;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class PublishingMatchRepositoryImpl implements PublishingMatchRepository {

    private static PublishingMatchRepositoryImpl instance;
    private final ConcurrentHashMap.KeySetView<Subscriber<Match>, Boolean> subscribers;
    private final ConcurrentHashMap.KeySetView<Subscriber<Match>, Boolean> unsubscribed;
    private final MatchRepository origin;

    private PublishingMatchRepositoryImpl(MatchRepository origin) {
        this.origin = origin;
        subscribers = ConcurrentHashMap.newKeySet();
        unsubscribed = ConcurrentHashMap.newKeySet();
    }

    public static synchronized PublishingMatchRepositoryImpl getInstance(MatchRepository origin) {
        if (instance == null) {
            instance = new PublishingMatchRepositoryImpl(origin);
        }
        return instance;
    }

    @Override
    public void addSubscriber(Subscriber<Match> subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void removeSubscriber(Subscriber<Match> subscriber) {
        unsubscribed.add(subscriber);
    }

    @Override
    public Match findById(String id) {
        return origin.findById(id);
    }

    @Override
    public boolean upsertAtomically(Match match, Predicate<Match> upsertCondition) {
        boolean upsertHappened = origin.upsertAtomically(match, upsertCondition);
        if (upsertHappened) {
            notifySubscribers(match);
        }
        return upsertHappened;
    }

    private void notifySubscribers(Match match) {
        subscribers.removeAll(unsubscribed);
        unsubscribed.clear();
        subscribers.forEach(subscriber -> subscriber.onUpdate(this, match));
    }

    @Override
    public void clear() {
        origin.clear();
    }
}
