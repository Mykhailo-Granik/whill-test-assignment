package com.whill.assignment.entity;

import com.whill.assignment.util.TestingMatchFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpsertMatchPredicateTest {

    private static final int UPDATE_TIME_STAMP = 42;

    private UpsertMatchPredicate underTest;

    @BeforeEach
    public void setup() {
        underTest = new UpsertMatchPredicate(
                TestingMatchFactory.createMatch(UPDATE_TIME_STAMP)
        );
    }

    @Test
    public void shouldReturnTrueWhenNoEntriesAboutMatchRecorded() {
        assertTrue(underTest.test(null));
    }

    @Test
    public void shouldReturnTrueWhenEntryWasModifiedEarlierThanCurrentUpdateTimestamp() {
        assertTrue(underTest.test(TestingMatchFactory.createMatch(UPDATE_TIME_STAMP - 10)));
    }

    @Test
    public void shouldReturnFalseWhenEntryWasModifiedLaterThanCurrentUpdateTimestamp() {
        assertFalse(underTest.test(TestingMatchFactory.createMatch(UPDATE_TIME_STAMP + 10)));
    }

}