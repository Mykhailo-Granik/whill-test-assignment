package com.whill.assignment.repository;

import com.whill.assignment.entity.Match;
import com.whill.assignment.publisher.Publisher;
import com.whill.assignment.subscriber.Subscriber;
import com.whill.assignment.util.TestingMatchFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PublishingMatchRepositoryImplTest {

    private PublishingMatchRepository underTest;
    private static TestingMatchRepository matchRepository;

    @BeforeAll
    public static void setup() {
        matchRepository = new TestingMatchRepository();
    }

    @Test
    public void whenUpsertHappensSubscribersShouldBeNotified() {
        underTest = publishingRepository(true);
        TestingSubscriber subscriber = addSubscriber();
        underTest.upsertAtomically(TestingMatchFactory.createMatch(0), match -> true);
        assertTrue(subscriber.onUpdateCalled);
    }

    private PublishingMatchRepositoryImpl publishingRepository(boolean upsertHappened) {
        matchRepository.upsertHappened = upsertHappened;
        return PublishingMatchRepositoryImpl.getInstance(matchRepository);
    }

    private TestingSubscriber addSubscriber() {
        TestingSubscriber subscriber = new TestingSubscriber();
        underTest.addSubscriber(subscriber);
        return subscriber;
    }

    @Test
    public void whenUpsertDoesNotHappenSubscribersShouldNotBeNotified() {
        underTest = publishingRepository(false);
        TestingSubscriber subscriber = addSubscriber();
        underTest.upsertAtomically(TestingMatchFactory.createMatch(0), match -> false);
        assertFalse(subscriber.onUpdateCalled);
    }

    public static class TestingMatchRepository implements MatchRepository {

        public boolean upsertHappened;

        @Override
        public Match findById(String id) {
            return null;
        }

        @Override
        public boolean upsertAtomically(Match match, Predicate<Match> upsertCondition) {
            return upsertHappened;
        }

        @Override
        public void clear() {

        }

    }

    private static class TestingSubscriber implements Subscriber<Match> {

        public boolean onUpdateCalled;

        @Override
        public void onUpdate(Publisher<Match> caller, Match context) {
            onUpdateCalled = true;
        }
    }
}
