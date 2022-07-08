package com.whill.assignment.controller;

import com.whill.assignment.entity.Match;
import com.whill.assignment.util.TestingMatchFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MatchControllerIT {

    private static final int UPDATE_TIME_STAMP = 42;
    @Autowired
    private TestRestTemplate template;

    @BeforeEach
    public void setup() {
        template.delete("/matches");
    }

    @Test
    public void whenThereIsOnlyOneMatchUpdateItsInformationShouldBeReturned() {
        Match match = TestingMatchFactory.createMatch(UPDATE_TIME_STAMP);
        putMatchUpdate(match);
        assertEquals(match, getMatchInfo(match.getId()).getBody());
    }

    private void putMatchUpdate(Match match) {
        template.put("/upsertMatch", match);
    }

    private ResponseEntity<Match> getMatchInfo(String matchId) {
        return template.getForEntity("/match/{id}", Match.class, matchId);
    }

    @Test
    public void shouldAlwaysReturnLatestUpdate() {
        Match latestUpdate = TestingMatchFactory.createMatch(UPDATE_TIME_STAMP);
        putMatchUpdate(latestUpdate);
        putMatchUpdate(TestingMatchFactory.createMatch(UPDATE_TIME_STAMP - 1));
        assertEquals(latestUpdate, getMatchInfo(latestUpdate.getId()).getBody());
    }

    @Test
    public void shouldReturnStatusNotFoundWhenThereIsNoInfoAboutTheMatch() {
        Match match = TestingMatchFactory.createMatch(UPDATE_TIME_STAMP);
        putMatchUpdate(match);
        ResponseEntity<Match> matchInfo = getMatchInfo(match.getId() + "some text");
        assertEquals(HttpStatus.NOT_FOUND, matchInfo.getStatusCode());
    }

    @Test
    public void subscribingToUpdateShouldTimeoutGracefully() throws ExecutionException, InterruptedException {
        ResponseEntity<Match> response = subscribeToUpdatesAsync("1", 500).get();
        assertEquals(HttpStatus.NOT_MODIFIED, response.getStatusCode());
    }

    private CompletableFuture<ResponseEntity<Match>> subscribeToUpdatesAsync(String matchId, long timeout) {
        CompletableFuture<ResponseEntity<Match>> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            completableFuture.complete(template.getForEntity("/match/{id}/updates?timeout={timeout}", Match.class, matchId, timeout));
            return null;
        });

        return completableFuture;
    }

    @Test
    public void subscribingShouldReturnResultOnUpdate() throws ExecutionException, InterruptedException {
        Match match = TestingMatchFactory.createMatch(UPDATE_TIME_STAMP);
        CompletableFuture<ResponseEntity<Match>> updatesCompletableFuture =
                subscribeToUpdatesAsync(match.getId(), 60000);
        // waiting for thread listening to updates to start up
        Thread.sleep(1000);
        putMatchUpdate(match);
        assertEquals(match, updatesCompletableFuture.get().getBody());
    }

}