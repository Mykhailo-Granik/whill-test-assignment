package com.whill.assignment.entity;

import com.whill.assignment.util.TestingMatchFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class MatchTest {

    @Test
    public void isNewerShouldReturnTrueIfMatchRepresentsNewerUpdate() {
        Assertions.assertTrue(
                TestingMatchFactory.createMatch(1).isNewer(
                        TestingMatchFactory.createMatch(0)
                )
        );
    }

    @Test
    public void isNewerShouldReturnFalseIfMatchRepresentsUpdateWithTheSameTimestamp() {
        assertFalse(
                TestingMatchFactory.createMatch(0).isNewer(
                        TestingMatchFactory.createMatch(0)
                )
        );
    }

    @Test
    public void isNewerShouldReturnFalseIfMatchRepresentsOlderUpdate() {
        assertFalse(
                TestingMatchFactory.createMatch(0).isNewer(
                        TestingMatchFactory.createMatch(1)
                )
        );
    }

}