package com.whill.assignment.repository;

import com.whill.assignment.entity.Match;
import com.whill.assignment.util.TestingMatchFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryMatchRepositoryTest {

    private InMemoryMatchRepository underTest;

    @BeforeEach
    public void setup() {
        underTest = InMemoryMatchRepository.getInstance();
    }

    @Test
    public void shouldInsertMatchesIntoEmptyStorageIfPredicateSaysSo() {
        Match match = TestingMatchFactory.createMatch(0);
        assertTrue(underTest.upsertAtomically(match, anyMatch -> true));
        Assertions.assertEquals(match, underTest.findById(match.getId()));
    }

    @Test
    public void shouldInsertMatchesIntoNonEmptyStorageIfPredicateSaysSo() {
        Match oldMatch = TestingMatchFactory.createMatch(0);
        underTest.upsertAtomically(oldMatch, anyMatch -> true);
        Match newMatch = TestingMatchFactory.createMatch(1);
        assertTrue(underTest.upsertAtomically(newMatch, anyMatch -> true));
        Assertions.assertEquals(newMatch, underTest.findById(oldMatch.getId()));
    }

    @Test
    public void shouldNotInsertMatchesIntoNonEmptyStorageIfPredicateSaysSo() {
        Match oldMatch = TestingMatchFactory.createMatch(1);
        underTest.upsertAtomically(oldMatch, anyMatch -> true);
        Match newMatch = TestingMatchFactory.createMatch(0);
        assertFalse(underTest.upsertAtomically(newMatch, anyMatch -> false));
        Assertions.assertEquals(oldMatch, underTest.findById(oldMatch.getId()));
    }

}